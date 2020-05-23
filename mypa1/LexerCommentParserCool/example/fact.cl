class Main inherits IO {
    fact(n: Int): Int {
        if (n = 0) then 1 
        else n*fact(n-1) fi
    }; 
    ant : String <- "a \0 ciao";
    g : Gam_e;
    m : Main;
    main() : Object { 
        {
            --m <- new Main;
            --g@IO.out_string("ciao\n");
            --out_int(fact(10));
            --out_string("\n");
            (let n1 : Int <- fact(3), n2 : Int <- fact(5) in {
                out_int(n1);
                out_string("\n");
                out_int(n2);
                out_string("\n");
                out_string(ant);
                out_string("\n");   
            });
            (let i : Int <- 0 in 
                while (i < 10) loop {
                    out_int(fact(i));
                    out_string("\n");
                    i <- i+1;  
                }
                pool 
            );                               
            g <- new Gam_e;
       }
    };
};

class Gam_e {
    init() : Int {
        5
    };


};
