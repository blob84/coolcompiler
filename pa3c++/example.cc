#include <stdio.h>

class Parent {
	public: 
		char *name = "parent";
		char *type = "person";
};

class Child: public Parent {
	public:
		char *name = "child";
};

int main(void) {
	void *p = new Child();
	Child *c = (Child *) p;

	printf("name=%s\t-\ttype=%s\n", c->name, c->type);

}
