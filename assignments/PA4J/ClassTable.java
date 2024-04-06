import java.io.PrintStream;

/**
 * This class may be used to contain the semantic information such as
 * the inheritance graph. You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.
 */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    private SymbolTable objectTable;
    // method table is a map from class name to a map from method name to method
    private SymbolTable methodTable;

    /**
     * Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String). Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable.addString("<basic class>");

        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes. There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables. You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        // cool_abort() : Object aborts the program
        // type_name() : Str returns a string representation
        // of class name
        // copy() : SELF_TYPE returns a copy of the object

        class_c Object_class = new class_c(0,
                TreeConstants.Object_,
                TreeConstants.No_class,
                new Features(0)
                        .appendElement(new method(0,
                                TreeConstants.cool_abort,
                                new Formals(0),
                                TreeConstants.Object_,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.type_name,
                                new Formals(0),
                                TreeConstants.Str,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.copy,
                                new Formals(0),
                                TreeConstants.SELF_TYPE,
                                new no_expr(0))),
                filename);

        // The IO class inherits from Object. Its methods are
        // out_string(Str) : SELF_TYPE writes a string to the output
        // out_int(Int) : SELF_TYPE " an int " " "
        // in_string() : Str reads a string from the input
        // in_int() : Int " an int " " "

        class_c IO_class = new class_c(0,
                TreeConstants.IO,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(new method(0,
                                TreeConstants.out_string,
                                new Formals(0)
                                        .appendElement(new formalc(0,
                                                TreeConstants.arg,
                                                TreeConstants.Str)),
                                TreeConstants.SELF_TYPE,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.out_int,
                                new Formals(0)
                                        .appendElement(new formalc(0,
                                                TreeConstants.arg,
                                                TreeConstants.Int)),
                                TreeConstants.SELF_TYPE,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.in_string,
                                new Formals(0),
                                TreeConstants.Str,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.in_int,
                                new Formals(0),
                                TreeConstants.Int,
                                new no_expr(0))),
                filename);

        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_c Int_class = new class_c(0,
                TreeConstants.Int,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(new attr(0,
                                TreeConstants.val,
                                TreeConstants.prim_slot,
                                new no_expr(0))),
                filename);

        // Bool also has only the "val" slot.
        class_c Bool_class = new class_c(0,
                TreeConstants.Bool,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(new attr(0,
                                TreeConstants.val,
                                TreeConstants.prim_slot,
                                new no_expr(0))),
                filename);

        // The class Str has a number of slots and operations:
        // val the length of the string
        // str_field the string itself
        // length() : Int returns length of the string
        // concat(arg: Str) : Str performs string concatenation
        // substr(arg: Int, arg2: Int): Str substring selection

        class_c Str_class = new class_c(0,
                TreeConstants.Str,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(new attr(0,
                                TreeConstants.val,
                                TreeConstants.Int,
                                new no_expr(0)))
                        .appendElement(new attr(0,
                                TreeConstants.str_field,
                                TreeConstants.prim_slot,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.length,
                                new Formals(0),
                                TreeConstants.Int,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.concat,
                                new Formals(0)
                                        .appendElement(new formalc(0,
                                                TreeConstants.arg,
                                                TreeConstants.Str)),
                                TreeConstants.Str,
                                new no_expr(0)))
                        .appendElement(new method(0,
                                TreeConstants.substr,
                                new Formals(0)
                                        .appendElement(new formalc(0,
                                                TreeConstants.arg,
                                                TreeConstants.Int))
                                        .appendElement(new formalc(0,
                                                TreeConstants.arg2,
                                                TreeConstants.Int)),
                                TreeConstants.Str,
                                new no_expr(0))),
                filename);

        /*
         * Do somethind with Object_class, IO_class, Int_class,
         * Bool_class, and Str_class here
         */
        this.objectTable.addId(TreeConstants.Object_, Object_class);
        this.objectTable.addId(TreeConstants.IO, IO_class);
        this.objectTable.addId(TreeConstants.Int, Int_class);
        this.objectTable.addId(TreeConstants.Bool, Bool_class);
        this.objectTable.addId(TreeConstants.Str, Str_class);
    }

    public ClassTable(Classes cls) {
        semantErrors = 0;
        errorStream = System.err;

        // Create a new symbol table
        this.objectTable = new SymbolTable();
        this.methodTable = new SymbolTable();

        this.objectTable.enterScope();
        this.methodTable.enterScope();
        installBasicClasses();

        // Add the classes to the symbol table
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            if (this.objectTable.lookup(c.getName()) != null) {
                this.semantError(c).println("Class " + c.getName() + " was previously defined.");
            } else {
                this.objectTable.addId(c.getName(), c);
            }
        }

        // Check for inheritance errors

        // Check for cycles in the inheritance graph
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            if (c.getName() != TreeConstants.Object_) {
                AbstractSymbol parent = c.getParent();
                while (parent != null && parent != TreeConstants.No_class) {
                    if (parent == c.getName()) {
                        semantError(c).println(
                                "Class " + c.getName()
                                        + " is involved in a cycle in the inheritance graph.");
                        break;
                    }
                    class_c parentClass = (class_c) this.objectTable.lookup(parent);
                    parent = parentClass.getParent();
                }
            }
        }

        // For each class, traverse the AST, gather all visible declarations in a symbol
        // table
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            this.objectTable.enterScope();
            Features features = c.features;
            SymbolTable methods = new SymbolTable();
            methods.enterScope();
            this.objectTable.addId(TreeConstants.self, new attr(0, TreeConstants.self, c.getName(), new no_expr(0)));
            for (int j = 0; j < features.getLength(); j++) {
                Feature feature = (Feature) features.getNth(j);
                if (feature instanceof attr) {
                    attr a = (attr) feature;
                    class_c attrClass = (class_c) this.objectTable.lookup(a.type_decl);
                    if (a.name == TreeConstants.self) {
                        this.semantError(c, a).println("'self' cannot be the name of an attribute.");
                    }
                    this.objectTable.addId(a.name, attrClass);
                } else if (feature instanceof method) {
                    method m = (method) feature;
                    methods.addId(m.name, m);
                }
            }
            // method table will need its own scan loop?
            this.methodTable.addId(c.getName(), methods);

            // Check each expression for type correctness
            for (int j = 0; j < features.getLength(); j++) {
                Feature feature = (Feature) features.getNth(j);
                if (feature instanceof attr) {
                    attr a = (attr) feature;
                    a.init.typeCheck(this, c);
                } else if (feature instanceof method) {
                    method m = (method) feature;
                    this.objectTable.enterScope();
                    for (int k = 0; k < m.formals.getLength(); k++) {
                        formalc formal = (formalc) m.formals.getNth(k);
                        if (Flags.semant_debug) {
                            System.err.println("Adding formal " + formal.name + ":" + formal.type_decl);
                        }
                        class_c formalClass = (class_c) this.objectTable.lookup(formal.type_decl);
                        this.objectTable.addId(formal.name, formalClass);
                    }
                    m.expr.typeCheck(this, c);
                    this.objectTable.exitScope();
                }
            }
            this.objectTable.exitScope();
        }
    }

    public method getMethod(AbstractSymbol className, AbstractSymbol name) {
        class_c c = (class_c) this.objectTable.lookup(className);
        while (c != null) {
            Features features = c.features;
            for (int i = 0; i < features.getLength(); i++) {
                Feature feature = (Feature) features.getNth(i);
                if (feature instanceof method) {
                    method m = (method) feature;
                    if (m.name == name) {
                        return m;
                    }
                }
            }
            c = (class_c) this.objectTable.lookup(c.getParent());
        }
        return null;
    }

    public AbstractSymbol getMethodReturnType(AbstractSymbol className, AbstractSymbol name) {
        class_c c = (class_c) this.objectTable.lookup(className);
        while (c != null) {
            Features features = c.features;
            for (int i = 0; i < features.getLength(); i++) {
                Feature feature = (Feature) features.getNth(i);
                if (feature instanceof method) {
                    method m = (method) feature;
                    if (m.name == name) {
                        return m.return_type;
                    }
                }
            }
            c = (class_c) this.objectTable.lookup(c.getParent());
        }
        return null;
    }

    public AbstractSymbol getObjectType(AbstractSymbol name) {
        if (Flags.semant_debug) {
            System.err.println("Looking up object type for " + name + ": " + this.objectTable.lookup(name));
        }
        class_c c = (class_c) (this.objectTable.lookup(name));
        return c.getName();
    }

    public boolean isSubtype(AbstractSymbol child, AbstractSymbol parent) {
        if (child == parent) {
            return true;
        }
        if (child == TreeConstants.SELF_TYPE) {
            return false;
        }
        class_c c = (class_c) this.objectTable.lookup(child);
        while (c != null) {
            if (c.getName() == parent) {
                return true;
            }
            c = (class_c) this.objectTable.lookup(c.getParent());
        }
        return false;
    }

    public boolean hasClass(AbstractSymbol name) {
        return this.objectTable.lookup(name) != null;
    }

    public AbstractSymbol getLCA(AbstractSymbol a, AbstractSymbol b) {
        class_c c = (class_c) this.objectTable.lookup(a);
        while (c != null) {
            if (isSubtype(b, c.getName())) {
                return c.getName();
            }
            c = (class_c) this.objectTable.lookup(c.getParent());
        }
        return null;
    }

    public void enterScope() {
        this.objectTable.enterScope();
    }

    public void exitScope() {
        this.objectTable.exitScope();
    }

    public void addId(AbstractSymbol name, Object value) {
        this.objectTable.addId(name, value);
    }

    /**
     * Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     *         to be printed.
     *
     */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    public PrintStream semantError(class_c c, TreeNode t) {
        return semantError(c.getFilename(), t);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t        the tree node
     * @return a print stream to which the rest of the error message is
     *         to be printed.
     *
     */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     *         to be printed.
     *
     */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
        return semantErrors != 0;
    }
}
