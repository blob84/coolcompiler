class C {
	a : Int <- 8;
	b : Bool;
	o : Object;
	init(x : Int, y : Bool) : C {
           {
		a <- x;
		if true then a+1 else a fi;
		let a: Int in a <- 1;
		case a of 
			id1: Int => a;
			id2: Bool => a=2;
		esac;
		isvoid true;
		self;
           }
	};
};

Class Main {
	main():C {
	  (new C).init(1,true)
	};

};

class B inherits C {
	init(x : Int, y : Bool) : Int {
		self	
	};

};
