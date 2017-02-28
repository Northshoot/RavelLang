# The Ravel Runtime

## Concepts

- Model class: the generated code for a Model
- Model base class: the runtime code for a streaming, local, or replicated model
- Controller class: the generated code for a Controller
- Interface class: the generated code for an Interface
- App dispatcher: the generated class tying everything together
- Driver: the platform runtime class that the dispatcher talks to
- Endpoint: an abstract object that represents a network endpoint
- Packet: an opaque object that represents a network packet from the AppDispatcher point of view

## Language Runtime

### Language implementation

#### Classes

The Ravel language is implemented on top of an abstract class-based object oriented language that
supports single-inheritance with explicit virtual methods, and no method overloading or operator overloading.
This has a straight forward mapping to Java, Python and C++.

In C, instances of classes are expressed as typedeffed structs and passed by pointer. The instance structure of
the parent class is the first element of the struct and by convention called `base`. Upcasting is written as `&self->base`,
downcasting is written as `(Foo*)self`.

Methods are mapped to functions which are prefixed with the class name (eg. a method `FooBar.frobnicate` is mapped to `foo_bar_frobnicate`),
whose first argument is a pointer to the instance, by convention called `self` (with the exception of some parts of generated
code where the implicit pointer has other names to avoid clasesh with Ravel code).
A constructor `foo_init` is generated that calls the base class constructor `base_init` (eg. `base_model_init`).
A destructor `foo_finalize` is also generated, and it calls the base class destructor `base_finalize` at the end.
Memory allocation is not handled by constructors and destructors (as in Rust/C++, but differently than, say, GObject or many
class abstractions in C), so that objects can be allocated statically or on the stack.

#### Arrays

The Ravel language assumes the abstract target-language supports Java-style arrays (dynamically instanced arrays that own
their memory and cannot be resized). Arrays must be able to query their length in O(1) time, and access random elements in
O(1). Arrays must be writable.

In C, arrays are implemented as a single block of memory, of size `(length * element_size) + sizeof(size_t)`. The first
`sizeof(size_t)` bytes encode the number of elements. One must use `ravel_array_new()`, `ravel_array_length` and `ravel_array_free`
to manipulate arrays, but can use the native `[]` operator to read and write elements.

#### Primitive types

The Ravel language assumes the abstract target-language supports unsigned 8 bit integers, signed 32 bit integers, IEEE 758 
double precision floating points, and immutable UTF-8 encoded strings. All of these have mostly straight forward representations
in all supported target languages.

### Model base classes

Each generated model class is made to inherit from one of `LocalModel`, `StreamingModel` or `ReplicatedModel`, which in
turn inherit from `BaseModel`. `BaseModel` defines the following API:

(using Rust-like syntax for memory management semantics)

#### API called from generated code:

- `init(&mut self, size : i32, record_size : i32)`

Allocates memory for `size` records, each sized `record_size`

- `all(&self): &[&Record]`

Returns an array of records. The memory for this array is owned by the model class and should not be freed. The return value
is an array of pointers, not an array of records.

NOTE: in Java, there are two methods: `all() : Record[]` (generated) and `all(T[]) : T[]` (runtime; `T` is a type parameter). This is because
you need a concrete instance of an array to create a generic array of the same type.

- `get(&self, idx : i32): &Record`

Returns a pointer to a record, or NULL if no record exists at this index. The memory for this record is owned by the model
class and should not be finalized or freed.

- `first(&self): &Record`, `last(&self) : &Record`

Returns the first (resp. the last) record in the model. Memory management is the same as `get()`.

- `delete(&mut self, idx : i32)`

Delete (finalize) the record with the given index in the model. **All** records are to be considered invalid after this call
(because they might be moved in the memory).

- `clear(&mut self)`

Clears the backing store of this model. All previously obtained records are finalized and freed, and become invalid
after this call.

- `allocate(&mut self): void*`

Allocate space for one record in the model. Memory for this record is owned by the model class and should not be finalized or freed.
This record is not initialized or zero-filled. Calling this function should be followed by record initialization code
immediately.

NOTE: this function does not exist in Java or Python (where memory management is automatic).

- `save(&mut self, &'model Record) : &Context`

Save a record to the model, and start sending it on the network.

- `add_endpoints(&mut self, &'static [&'static str])`

Inform the model of the list of endpoints that the code should send records to. This is called at startup by the generated code.

NOTE: In C, the endpoint list is a NULL terminated array of static strings.

#### Abstract API implemented by generated code:

- `abstract create(&mut self) : &Record`

Construct a new empty `Record` object. Memory for this record is owned by the model class and should not be finalized or freed.

NOTE: there is no vtable slot for this method in C, because the runtime should never need to call it.

- `abstract unmarshall(&self, data : &[u8], length : usize, Endpoint endpoint) : &Record`

Construct a new `Record` object from `data` bytes coming from the network.

- `abstract marshall(&self, &Record) : [u8]`

Converts the record to its serialized form. This method returns newly allocated memory, which the caller is responsible to free
with **`ravel_array_free`** or equivalent (not `free`).

- `abstract dispatch_full(&self, &'model Context)`

Notify any listening controller of a `full` event.

- `abstract dispatch_arrived(&self, &'model Context)`

Notify any listening controller of an `arrived` event.

- `abstract dispatch_departed(&self, &'model Context)`

Notify any listening controller of a `departed` event.

- `abstract dispatch_save_done(&self, &'model Context)`

Notify any listening controller of a `save_done` event.

#### API called from dispatcher:

- `record_arrived(&mut self, &'dispatcher Packet, &'dispatcher Endpoint)`

Inform the model that a record arrived from the given endpoint.

- `record_departed(&mut self, &'dispatcher Packet, &'dispatcher Endpoint)`

Inform the model that a record was sent successfully to the given endpoint.

- `record_saved_durably(&mut self, &'dispatcher Packet)`

FIXME: ???
(In Java, this calls `notify_save_done`. But nobody ever calls it...)

- `record_saved_endpoint(&self self, &'dispatcher Packet, &'dispatcher Endpoint)`

FIXME: ???

- `record_failed_to_send(&mut self, &'dispatcher Packet, &'dispatcher Endpoint, Error)`

Inform the model that a record could not be sent to the given endpoint (so that the model can retry transmission if desired).

- `endpoint_connected(&mut self, &'dispatcher Endpoint)`

FIXME: ???

### Dispatcher

The dispatcher is the where everything is linked together. There is one `AppDispatcher` class which is generated, and which
inherits from `BaseDispatcher`.

The dispatcher is strongly associated with a thread of execution, and all model and controller calls are expected to happen
on the dispatcher thread. In Java, this is enforced with an event queue internal to the dispatcher. In C, the driver is
responsible to make sure that the calls happen in the right place.

NOTE: in Java, `BaseDispatcher` is called `AbstractDispatcher`.

#### Base Dispatcher class

- `init(&mut self)`

Empty

- `set_driver(&mut self, &'static Driver)`

Associate the dispatcher with the driver. Called by the platform initialization code at application startup.

- `get_endpoints_by_name(&self, &str) : &[&Endpoint]`

Retrieve the current list of endpoints corresponding to those names. This just forwards to the driver.

NOTE: in C, this returns a NULL terminated array.

- `send_data(&mut self, Packet, &'dispatcher Endpoint) : Error`

Send a packet out. The call takes ownership of the packet, so the caller must not finalize it.

#### Generated dispatcher class

- `constructor(&mut self)`

Initialize the dispatcher and construct all the associated objects.

- `get_app_name(&self): &str`

Returns the name of the space that corresponds to this code.

- `add_all_endpoints()`

Initializes all endpoint names on all models. This is called by platform initialization code after setting
the driver.

- `started(&self)`, `stopped(&self)`, `restarted(self)`

Dispatch the corresponding system event.

#### Base Dispatcher methods implemented by the generated dispatcher

- `abstract data_received(&self, &'driver Packet, &'driver Endpoint)`

Notify the dispatcher that a packet has been received. This method is called by the driver and calls into the model.

- `abstract send_done(&self, Error, &'driver Packet, &'driver Endpoint)`

Notify the dispatcher that a packet has been send out (successfully or not). This method is called by the driver and
calls into the model.

### Driver

The driver is part of the platform runtime. There is one driver class in each platform (`RavelPosixDriver`, `RavelContikiDriver`,
etc.), and they all inherit from `RavelDriver`.

NOTE: because only one platform can be linked in at a time, in C we avoid the virtual call and have each driver just
provide methods with the right names.
NOTE: in Java, there is no base driver class. Instead, drivers implement `DriverAPI`.

Drivers expose the following methods:

- `init(&'static Dispatcher)`

Initialize the driver.

- `get_endpoints_by_name(&self, &str) : &[&Endpoint]`

Retrieve the current list of endpoints corresponding to those names. This list is subject to change dynamically as
new endpoints connect to the system.

- `send_data(&mut self, Packet, &'dispatcher Endpoint) : Error`

Send data to the specific endpoint. The driver assumes ownership of the given packet, which should not be freed by
the caller.

- `app_dispatcher_ready(&mut self)`

Signals that the app dispatcher has finished initialization. This is called at the end of the initialization sequence
and starts the application.

### Intrinsics

Intrinsics are a magic and hand-wavy way to do system libraries so that the code generators can have special code for
each library calls.

A code generator (aka a `IRTranslator`) can implement special code for an intrinsic, or can defer to a library call. In Java,
intrinsics are implemented in `org.stanford.ravel.rrt.lang.Instrinsics`. In C, they are prefixed with `ravel_intrinsic`.

Currently, the following intrinsics are defined:

- `array_new(length : i32) : [T]`: create a new array of the given type. Special code in C and Java.
- `array_length(&[T]) : i32`: read the length of an array. Library call in C and Java.
- `strlen(&str) : i32`: read the length of a string. Library call in Java, special code in C (direct call to `strlen`).
- `extract_uint16(&[u8], pos : i32) : i32`, `extract_int32(&[u8], pos : i32) : i32`, `extract_timestamp(&[u8], pos : i32) : i32`,
  `extract_error_msg(&[u8], pos : i32) : Error`, `extract_double(&[u8], pos : i32) : f64`,
  `extract_bool(&[u8], pos : i32) : bool`, `extract_byte(&[u8], pos : i32) : u8`:
  read out the corresponding primitive type from serialized form in the given byte array at the given position. Library call in C and Java.
- `extract_str(&[u8], pos : i32, length : i32) : str`: read out a string from the given byte array at the given position.
  This returns a newly allocated string the caller is responsible to free. Library call in C and Java.

### Serialization API

Record serialization code relies on a class called `GrowableByteArray`, which exposes a dynamically growable (power of 2 sized)
array of bytes, with methods to write structured data into it. This class has the following methods:

- `create(): GrowableByteArray`

Create a new empty `GrowableByteArray`. This returns a stack allocated object (not a pointer!), which the caller is responsible
to store somewhere and later finalize.

- `init(&mut self)`

Initialize the array to a default size.

- `write_int32(&mut self, i32)`, `write_uint16(&mut self, i32)`, `write_error_msg(&mut self, Error)`, `write_str(&mut self, &str)`,
  `write_byte(&mut self, u8)`, `write_timestamp(&mut self, i32)`, `write_double(&mut self, f64)`, `write_bool(&mut self, bool)`,
  `write_byte_array(&mut self, &[u8])`
  
Write the corresponding primitive type in serialized form to the array
  
- `get_byte_array(&self): [u8]`

Extract the serialized byte array from the buffer. This method returns a copy of the internal buffer, which the caller
is responsible to free.

# Old stuff

How to add a driver

All driver have app dispatcher ready function

Each driver implements main loop

Event queue have to be in the driver 

the AD queues events in the driver 

BLE?



AppDispatcher API