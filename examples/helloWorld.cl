class Main inherits IO {
	c : C <- new C;
	--a: Int <- 3;
	--s : String <- "ciao";
  main() : Object { {
	c.init(2, 3, 4);
    out_string("Hello, world.\n"); }
  } ;
} ; 

class C  {
	--h: Int <- 21;
	b: Int <- 6;
	--d: String <- "ciao mondo";
	--e: String <- "come va";
	--f: Bool <- true;
	k : C <- new C;
	init(h: Int, i: Int, j: Int): Int { {
		let x: Int, x: Int,y: Int,z: Int,a: Int,b: Int,c: Int,d: Int,e: Int,f: Int in x <- h;
		--b <- j;
		--self.m(7,8,9);	
		3;  }
	};

	m(z: Int, x: Int, y: Int): Int {
		x
	};
};

class D inherits C {
	l: Int <- 30+8;
	m: Bool <- true;
	init(h: Int, i: Int, j: Int): Int {
		case 8 of b: String => 500; a: C => 2; esac
	};
};
