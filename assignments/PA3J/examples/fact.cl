class Main inherits A2I {
    main() : Object {
        (new IO).out_string(i2a(factRecur(a2i((new IO).in_string()))).concat("\n"))
    };

    i2cT(i : Int) : String {
        if i = 0 then "0" else
        if i = 1 then "1" else
        if i = 2 then "2" else
        if i = 3 then "3" else
        if i = 4 then "4" else
        if i = 5 then "5" else
        if i = 6 then "6" else
        if i = 7 then "7" else
        if i = 8 then "8" else
        if i = 9 then "9" else
        { abort(); ""; }  -- the "" is needed to satisfy the typchecker
            fi fi fi fi fi fi fi fi fi fi
    };

    i2aT(i : Int) : String {
	if i = 0 then "0" else 
        if 0 < i then i2a_auxT(i) else
          "-".concat(i2a_auxT(i * ~1)) 
        fi fi
    };

    i2a_auxT(i : Int) : String {
        if i = 0 then "" else 
	    (let next : Int <- i / 10 in
		    i2a_auxT(next).concat(i2cT(i - next * 10))
	    )
        fi
    };

    a2iT(s : String) : Int {
        if s.length() = 0 then 10 else
	        if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
                if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
                    3
        fi fi fi
     };
    
    a2i_auxT(s : String) : Int {
	    (let int : Int <- 0 in	
        {	
            (let j : Int <- s.length() in
                (let i : Int <- 0 in
                    while i < j loop
                    {
                        int <- int * 10 + c2i(s.substr(i,1));
                        i <- i + 1;
                    }
                    pool
                )
            );
            int;
        }
        )
    };

    factRecur(i: Int) : Int {
        if (i = 0) then 1 else i * fact(i - 1) fi
    };

    fact(i: Int) : Int {
        let fact: Int <- 1 in {
            while (not (i = 0)) loop {
                fact <- fact * i;
                i <- i - 1;
            } pool;
            fact;
        }
    };
};