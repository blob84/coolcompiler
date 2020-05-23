#include <iostream>
#include <vector>
#include <cstdlib>
#include <string>
#include <typeinfo>

using namespace std;
 
class C {
	public:
		int type;
		int i;
		C(int n): i(n) { };
		void p() { cout << "im C " << i <<endl; };
};

class D: public C {
	public:
		int j;
		D(int n): C(n) { type = 0; };
		void p() { cout << "im D " << i <<endl; };
}; 
 
class E: public C {
	public:
		int j;
		E(int n): C(n) { type = 1; };
		void p() { cout << "im E " << i <<endl; };
}; 

int main ()
{
/*    int v = 0;

  	vector < vector<int> > tube;
	tube = vector< vector<int> >(10, vector<int>(1, 11));	
	const int si = tube.size();
	int a[si];
*/
	vector<int> v(10, 1);
  	vector < vector<int> > tube;
	tube = vector< vector<int> >(10, vector<int>(2));	

	vector< vector<int> > *vv = &tube;

	C *d = new D(1);  
/*	C *e = new E(2);
	((D *) d)->p(); 
	cout << "D type: " << d->type << endl;
	cout << "E type: " << e->type << endl;
	delete d;
	delete e;
*/
	D *ds = new D[20];
	ds[0] = d;	delete d;

/*	vector<int> *vvv = &v;

	(*vvv)[5] = 8;
	for (int i : v)
		cout << i << ", ";
	cout << endl;
	int j = 0;
	for (int i = 0; i < vv->size(); i++) 
		(*vv)[i].push_back(j++); 

	(*vv)[0][1] = 33;

	for (vector<int> v : *vv) {
		for (int i : v)
			cout << i << ", ";
		cout << endl;
	}

	void *p = (void *) new string("ciao mondo\n"); 
	string *s = (string *) p;

	cout << *s;
	const int size = tube.size();
	delete s;

	c o1 = c(3);
	o1.p();

	c o2 = d(8);
	c *p2 = new d(8);
	d *p3 = static_cast<d *>(p2);
	d *p8 = p3;
	p3->p();
	d o4 = *p3;
	o4.p();
	cout << typeid(o4).name() << endl;
  for(int i = 0; i < 5; i++)
  {
    vector < int > w;
    tube.push_back( w );
    for(int j = 0; j < 5; j++)
    {
      tube[i].push_back( v++ );
    }
  }
  for (size_t i = 0; i < tube.size(); i++)
    for (size_t j = 0; j < tube[i].size(); j++)
        cout << "tube[" << i << "][" << j << "] = " << tube[i][j] << endl;   
	*/
}
