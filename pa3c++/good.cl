class C {
	a : Int;
	init(x : Int) : C {
           {
		a <- x;
		self;
           }
	};
	b : Int <- init+3;

};

Class Main {
	main():C {
	  (new C).init(1)
	};
};


