#ifndef HASHSET_H_INCLUDED
#define HASHSET_H_INCLUDED


#ifdef __GNUC__
#include <ext/hash_set>
#else
#include <hash_set>
#endif


namespace std{ using namespace __gnu_cxx; }

/**
 * Envelop class for an hashset.
 * If in the future the hash_set used is changed,
 * modifications only in this class are needed
 */
template<typename V>
class HashSet{
    protected:
        // the map
        std::hash_set<V> set;

    public:
        // --------------
        //  Constructors
        HashSet() throw(){}

        // -------------------------
        //  some usefull operations
        void clear() throw(){ set.clear(); }

        size_t size() const throw(){ return set.size(); }
        bool isEmpty()const  throw(){ return set.empty(); }
        bool contains(const V& value)const  throw(){ return set.find(value)!=map.end(); }
        bool insert(const V& value) throw(){ return set.insert(value).second; }
        void remove(const V& value) throw(){ set.erase(value); }

    // --------------------
    //  ---- ITERATOR ----
    // --------------------
    class HashSetIterator {
        protected:
            typename std::hash_set<V>::iterator it;

        public:

            HashSetIterator() throw(){}

            HashSetIterator(typename std::hash_set<V>::iterator pos) throw(){
                it = pos;
            }


             V& operator*(){ return *it; }
             V& operator->(){ return it.operator->(); }
             void operator++() throw(){ it++; }     // prefix
             void operator++(int) throw(){ ++it; }  // Postfix

             friend bool operator==(const HashSetIterator& it1, const HashSetIterator& it2) throw(){
                 return it1.it == it2.it;
             }

             friend bool operator!=(const HashSetIterator& it1, const HashSetIterator& it2) throw(){
                 return it1.it != it2.it;
             }
     };
     typedef HashSetIterator iterator;

     iterator begin()  throw(){ return iterator(map.begin()); }
     iterator end()  throw(){return iterator(map.end()); }
 };




#endif // HASHSET_H_INCLUDED
