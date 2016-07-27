#include <iostream>
#include <cstdint>
#include <vector>
using namespace std;
enum Error {SUCCESS, FULL};

class Model
{
private:
    vector<record> queue;
    uint32_t size;

public:
    struct record{
        uint32_t timestamp;
        uint32_t temperature;
        uitn32_t voltage;
    } ;

    uint32_t getSize() { return size; }

    Error add(record r){
        Error error = SUCCESS;
        if (size - queue.size()>0 ) {
            queue.push_back(r);
        } else {
            error = FULL;
        }
        return error;
    }

    char*  getNext(){
        return static_cast<char*>(static_cast<void*>(&queue.));
    }
};

int main() {
    cout << "Hello, World!" << endl;
    return 0;
}