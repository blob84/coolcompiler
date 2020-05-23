class C  {
	a : Int;
	b : Bool;
	init(x : Int, y : Bool) : C {
           {
		a <- x;
		c <- y;
		self;
           }
	};
	init2(x : Int) : C {
           {
		a <- x;
		case a of 
			id1: Int => a;
			id2: Bool => a=2;
			id4: Int => 1+2;
		esac;
		self;
           }
	};
	c : Bool;

};

Class Main {
	main():C {
	 {
	  (new C).init(1,"a");
	  (new C).init(1,true,3);
	  (new C).iinit(1,true);
	  (new C);
	 }
	};
};

