#ifndef HASHMAP_H_INCLUDED
#define HASHMAP_H_INCLUDED


#ifdef __GNUC__
#include <ext/hash_map>
#else
#include <hash_map>
#endif


namespace std{ using namespace __gnu_cxx; }



/**
 * Envelop class for an hashmap.
 * If in the future the hash_map used is changed,
 * only this class modifications are needed
 */
template<typename K, typename V>
class HashMap{
    protected:
        // the map
        std::hash_map<K,V,std::hash<K> > map;

    public:



    // --------------------
    //  ---- ITERATOR ----
    // --------------------
    class HashMapIterator {
        protected:
            typename std::hash_map<K,V,std::hash<K> >::iterator it;

        public:

            HashMapIterator() throw(){}

            HashMapIterator(typename std::hash_map<K,V,std::hash<K> >::iterator pos) throw(){
                it = pos;
            }


             std::pair<const K,V>& operator*() const{ return *it; }
             std::pair<const K,V>* operator->() const{ return it.operator->(); }
             void operator++() throw(){ it++; }     // prefix
             void operator++(int) throw(){ ++it; }  // Postfix

             friend bool operator==(const HashMapIterator& it1, const HashMapIterator& it2) throw(){
                 return it1.it == it2.it;
             }

             friend bool operator!=(const HashMapIterator& it1, const HashMapIterator& it2) throw(){
                 return it1.it != it2.it;
             }
     };
     typedef HashMapIterator iterator;

     iterator begin() { return iterator(map.begin()); }
     iterator end() {return iterator(map.end()); }




        // --------------
        //  Constructors
        HashMap() throw(){}
        HashMap(const bool*& cmp) throw():map(cmp){}

        // -------------------------
        //  some usefull operations
        void clear() throw(){ map.clear(); }

        V& operator[](const K& key){ return map[key]; }
        const V& get(const K& key) const{ return map.find(key)->second; }
        size_t size() const  throw(){ return map.size(); }
        bool isEmpty()const  throw(){ return map.empty(); }
        bool containsKey(const K& key) const throw(){ return map.find(key)!=map.end(); }
        iterator find(const K& key){ return iterator(map.find(key)); }
        void remove(iterator it){ remove(it->first); }
        void remove(const K& key) throw(){
            typename std::hash_map<K,V,std::hash<K> >::iterator it = map.find(key);
            if (it!=map.end()) map.erase(it);
        }

 };


struct string_hash : public std::unary_function<std::string,size_t>{
  size_t operator() (const std::string &v) const{
    return std::hash<char*>()(v.c_str());
  }
};
//typedef hash_set<std::string,string_hash> string_hash_set;



template<typename V>
class HashMap<std::string, V>{
    protected:
        // the map
        std::hash_map<std::string,V,string_hash> map;

    public:



    // --------------------
    //  ---- ITERATOR ----
    // --------------------
    class HashMapIterator {
        protected:
            typename std::hash_map<std::string,V,string_hash>::iterator it;

        public:

            HashMapIterator() throw(){}

            HashMapIterator(typename std::hash_map<std::string,V,string_hash>::iterator pos) throw(){
                it = pos;
            }


             std::pair<const std::string,V>& operator*() const{ return *it; }
             std::pair<const std::string,V>* operator->() const{ return it.operator->(); }
             void operator++() throw(){ it++; }     // prefix
             void operator++(int) throw(){ ++it; }  // Postfix

             friend bool operator==(const HashMapIterator& it1, const HashMapIterator& it2) throw(){
                 return it1.it == it2.it;
             }

             friend bool operator!=(const HashMapIterator& it1, const HashMapIterator& it2) throw(){
                 return it1.it != it2.it;
             }
     };
     typedef HashMapIterator iterator;

     iterator begin() { return iterator(map.begin()); }
     iterator end() {return iterator(map.end()); }




        // --------------
        //  Constructors
        HashMap() throw(){}
        HashMap(const bool*& cmp) throw():map(cmp){}

        // -------------------------
        //  some usefull operations
        void clear() throw(){ map.clear(); }

        V& operator[](const std::string& key){ return map[key]; }
        const V& get(const std::string& key) const{ return map[key]; }
        size_t size() const  throw(){ return map.size(); }
        bool isEmpty()const  throw(){ return map.empty(); }
        bool containsKey(const std::string& key) const throw(){ return map.find(key)!=map.end(); }
        iterator find(const std::string& key){ return iterator(map.find(key)); }
        void remove(iterator it){ remove(it->first); }
        void remove(const std::string& key) throw(){
            typename std::hash_map<std::string,V,string_hash>::iterator it = map.find(key);
            if (it!=map.end()) map.erase(it);
        }

 };



#endif // HASHMAP_H_INCLUDED
