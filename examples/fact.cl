class Main inherits IO {
    main() : SELF_TYPE {
	{
	    out_int(fact(4));
	    out_string("\n");
	}
    };

	fact(n: Int): Int {
		if n < 1 then 1 else n * fact(n-1) fi
	};
};
