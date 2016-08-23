//#ifdef __GNUC__
//#include <ext/hash_map>
//#else
//#include <hash_map>
//#endif

//namespace std{ using namespace __gnu_cxx; }


#include "../Utilities/HashMap.h"

#include <iostream>
#include <string>



int main2(){

//    std::hash_map<int,int> hm;
    HashMap<char*,int> map;
    map["5"] = 50;
    map["6"] = 60;
    map["1"] = 10;
    map["4"] = 40;
    map["7"] = 70;
    map["3"] = 30;
    if (map.containsKey("5")) map.remove("5");
    map.remove("7");
    map.remove("8");

    HashMap<char*,int>::iterator it = map.begin();
    for(; it!=map.end(); ++it){
        std::cout << (*it).first << ": " << (*it).second << "\n";
    }
//    for(int i=0; i<10; ++i)
//        if (map.containsKey(i))
//            std::cout << i << ": " << map[i] << "\n";
    return 0;
}
