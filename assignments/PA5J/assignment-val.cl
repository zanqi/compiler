class Main inherits IO {
  f(x : Int, y : Int) : Object { {
    out_int(x);
    out_int(y);
  } };
  f2() : Int {
    0
  };
  main() : Object {
    let x : Int <- 2 in {
      f(x, x);
      f(x <- 3, x <- 4);
      out_int(x);
    }
  };
};
