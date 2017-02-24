# Ravel Language #

## Ravel Concepts ##

##Programming with Ravel ##

## Syntax ##

### Keywords ###



### Programming Ravel ###

#### System API ####

#### Model API ####

#### Space API ####

#### View API ####

#### System API ####

#### Interface API ####
1) interfaces now receive an "instance id" as the first argument to the constructor (after self in C)

[6:50]  
2) interfaces must have a field in their struct called __id, and they must store the instance id in there in the constructor, if they have any event

[6:51]  
3) interfaces must use the template <dispatch_event(interface, eventName, params)> (where params is a list of additional event parameters) to dispatch events

[6:51]  
4) interfaces (and models) receive additional parameters (as declared in ravel) as part of their constructors

[6:52]  
5) you can no longer rely on interfaces.configuration being made only of constants, as it could refer to one of the parameters, which are only available in the constructor (so you must copy the stuff over to the instance struct)

[6:52]  
6) same for model.size, but the templates have been updated

[6:53]  
7) you can now have two controllers of the same type, connected to different interfaces, in the same space

[6:53]  
:sunglasses:  you still cannot have two models of the same type in the same space, and now you get a compile error if you try

#### Template API ####
Ravel uses StringTemplate developed by Terence Parr. Please refer http://www.stringtemplate.org/ for details.
Below we elaborate on Ravel's template structure.

##### C Templates ######
Includes and flags 
<pre>
<code> extra_cflags() </code>
</pre>
 
<pre>
<code> extra_ldflags() </code>
</pre>
  
<pre>
<code> extra_includes() </code>
</pre>
   

<pre>
<code> c_file(includes,name,interface)  </code>
</pre>
 
<pre>
<code> < interface.configuration.led_id; format="literal" > </code>
</pre>
 
<pre>
<code> < begin_source(name) > </code>
</pre>

<pre>
<code> < includes:do_include(); separator="\n" > </code>
</pre>
 
<pre>
<code> < name; format="function" >_ </code>
</pre>
 
<pre>
<code> < begin_source(name) > </code>
</pre>
 

 
<pre>
<code> < includes:do_include(); separator="\n" > </code>
</pre>
 
Define the h file:
<pre>
<code> h_file(includes,name,interface)  </code>
</pre>

<pre>
<code> < begin_header("LIBRARY", name) >  </code>
</pre>

Begin extern declaration:

<pre>
<code> < begin_extern_c() > </code>
</pre>
 
Any functions same syntax as in C file template
 
<pre>
<code> < end_extern_c() > </code>
</pre>


## Extending Ravel ##

### Writing Interface ###
Interface have two components: ravel code and template.

The <code>Interface</code> implementation in Ravel has three scopes:
 + <code>configuration</code> any static parameter that is set
 + <code>implementation</code> path to implementation template
 + declarations:
    * <code>def</code> command 
    + <code>event</code>  event (callback)
 
Each interface has one (or more) corresponding templates with mandatory functions:
+ <code>_init</code>  has initialization code
+ <code>_finalize</code> has clean up code that is called from stopping event
+ any other command/event that is declared in the interface

Moreover, the template group has following mandatory templates:
<pre><code>extra_cflags() ::=<<
>>
extra_ldflags() ::=<<
>>
extra_includes() ::=<<
>></code></pre>

That define system dependencies.

Interface is defined in Ravel language:

<pre><code>
interface Led(led_num: int32):
    configuration:
        led_id = led_num
        
    implementation:
        c = "rlib/nrf52/led.stg"

    def init()
    def on()
    def off()
    def toggle()
</code></pre>

The corresponding 

<pre><code>
</code></pre><pre><code>


#### Example Led ####
Example interface for LED implemented for NRF52 device

<pre><code>
interface Led(led_num: int32):
    configuration:
        led_id = led_num
    implementation:
        c = "rlib/nrf52/led.stg"

    def init()
    def on()
    def off()
    def toggle()
</code></pre>



<pre><code></code></pre>
<pre><code></code></pre>
<pre><code></code></pre>

### Writing Language Runtime ###

### Writing Platform Runtime ###

### Writing Driver ###
 
 