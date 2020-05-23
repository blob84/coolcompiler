
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)

class Main inherits IO {
	a: Int <- 36;
  main():Int { m(a <- 8) };
	m(x: Int): Int { 
		let y: Int <- 8 in 1
	};
};

