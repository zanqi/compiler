class Main inherits A2I {
    main() : Object {
        (new IO).out_string(i2a(factT(a2i((new IO).in_string()))).concat("\n"))
    };

    factRecur(i: Int) : Int {
        if (i = 0) then 1 else i * fact(i - 1) fi
    };

    factT(i: Int) : Int {
        i
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