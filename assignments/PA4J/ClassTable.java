import java.io.PrintStream;

import java_cup.runtime.Symbol;

/**
 * This class may be used to contain the semantic information such as
 * the inheritance graph. You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.
 */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    // class table is a map from class name to class
    private SymbolTable classTable;
    // object table is a map from class name to a map from object name to object
    private SymbolTable objectTable;
    // method table is a map from class name to a map from method name to method
    private SymbolTable methodTable;
    private class_c[] basicClasses;

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
        this.classTable.addId(TreeConstants.Object_, Object_class);
        this.classTable.addId(TreeConstants.IO, IO_class);
        this.classTable.addId(TreeConstants.Int, Int_class);
        this.classTable.addId(TreeConstants.Bool, Bool_class);
        this.classTable.addId(TreeConstants.Str, Str_class);
        this.classTable.addId(TreeConstants.prim_slot, new class_c(0, TreeConstants.prim_slot, TreeConstants.Object_, new Features(0), filename));
        this.basicClasses = new class_c[] { Object_class, IO_class, Int_class, Bool_class, Str_class };
    }

    public ClassTable(Classes cls) {
        semantErrors = 0;
        errorStream = System.err;

        this.classTable = new SymbolTable();
        this.objectTable = new SymbolTable();
        this.methodTable = new SymbolTable();

        this.classTable.enterScope();
        this.objectTable.enterScope();
        this.methodTable.enterScope();
        installBasicClasses();

        // Add the classes to the symbol table
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            if (this.classTable.lookup(c.getName()) != null) {
                this.semantError(c).println("Class " + c.getName() + " was previously defined.");
            } else {
                this.classTable.addId(c.getName(), c);
            }
        }

        if (this.classTable.lookup(TreeConstants.Main) == null) {
            this.semantError().println("Class Main is not defined.");
        }

        // Check illegal inheritance
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            if (c.getParent() == TreeConstants.Int || c.getParent() == TreeConstants.Bool
                    || c.getParent() == TreeConstants.Str || c.getParent() == TreeConstants.SELF_TYPE) {
                this.semantError(c).println("Class " + c.getName() + " cannot inherit from " + c.getParent() + ".");
            } else if (c.getName() == TreeConstants.SELF_TYPE) {
                this.semantError(c).println("Class " + c.getName() + " cannot be redefined.");
            } else if (c.getName() == TreeConstants.No_class) {
                this.semantError(c).println("Class " + c.getName() + " cannot be redefined.");
            } else if (this.classTable.lookup(c.getParent()) == null) {
                this.semantError(c).println("Class " + c.getName() + " inherits from an undefined class " + c.getParent() + ".");
            }
        }

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
                    class_c parentClass = (class_c) this.classTable.lookup(parent);
                    if (parentClass == null) {
                        break;
                    }
                    parent = parentClass.getParent();
                }
            }
        }

        // For each class, traverse the AST, gather all visible declarations in objects
        // and methods tables

        // append basic classes to the class list
        Classes cls2 = (Classes) cls.copy();
        for (int i = 0; i < this.basicClasses.length; i++) {
            cls2.appendElement(this.basicClasses[i]);
        }

        for (int i = 0; i < cls2.getLength(); i++) {
            class_c c = (class_c) cls2.getNth(i);
            SymbolTable attrs = new SymbolTable();
            attrs.enterScope();
            SymbolTable methods = new SymbolTable();
            methods.enterScope();
            attrs.addId(TreeConstants.self, c.getName());
            Features features = c.features;
            for (int j = 0; j < features.getLength(); j++) {
                Feature feature = (Feature) features.getNth(j);
                if (feature instanceof attr) {
                    attr a = (attr) feature;
                    if (a.name == TreeConstants.self) {
                        this.semantError(c, a).println("'self' cannot be the name of an attribute.");
                    }

                    attrs.addId(a.name, a.type_decl);
                } else if (feature instanceof method) {
                    method m = (method) feature;
                    methods.addId(m.name, m);
                }
            }
            this.objectTable.addId(c.getName(), attrs);
            this.methodTable.addId(c.getName(), methods);
        }

        // Check each expression for type correctness
        for (int i = 0; i < cls.getLength(); i++) {
            class_c c = (class_c) cls.getNth(i);
            this.classTable.enterScope();
            this.classTable.addId(TreeConstants.SELF_TYPE, c);
            Features features = c.features;
            for (int j = 0; j < features.getLength(); j++) {
                Feature feature = (Feature) features.getNth(j);
                if (feature instanceof attr) {
                    attr a = (attr) feature;
                    // check for attempt to override parent's attribute
                    AbstractSymbol parent = c.getParent();
                    while (parent != null && parent != TreeConstants.No_class) {
                        class_c parentClass = (class_c) this.classTable.lookup(parent);
                        SymbolTable parentAttrs = (SymbolTable) this.objectTable.lookup(parent);
                        if (Flags.semant_debug) {
                            System.err.println("Checking for attribute " + a.name + " in " + parent);
                        }
                        if (parentAttrs != null && parentAttrs.lookup(a.name) != null) {
                            this.semantError(c, a).println(
                                    "Attribute " + a.name + " is an attribute of an inherited class.");
                            break;
                        }
                        parent = parentClass.getParent();
                    }
                    a.init.typeCheck(this, c);
                } else if (feature instanceof method) {
                    method m = (method) feature;
                    SymbolTable attrs = (SymbolTable) this.objectTable.lookup(c.getName());
                    attrs.enterScope();
                    for (int k = 0; k < m.formals.getLength(); k++) {
                        formalc formal = (formalc) m.formals.getNth(k);
                        attrs.addId(formal.name, formal.type_decl);
                    }
                    m.expr.typeCheck(this, c);
                    attrs.exitScope();
                }
            }
            this.classTable.exitScope();
        }
    }

    public method getMethod(AbstractSymbol className, AbstractSymbol name) {
        class_c c = (class_c) this.classTable.lookup(className);
        while (c != null) {
            if (Flags.semant_debug) {
                System.err.println("Looking up method " + name + " in " + c.getName());
            }
            SymbolTable methods = (SymbolTable) this.methodTable.lookup(c.getName());
            if (methods != null) {
                method m = (method) methods.lookup(name);
                if (m != null) {
                    return m;
                }
            }
            c = (class_c) this.classTable.lookup(c.getParent());
        }
        return null;
    }

    public AbstractSymbol getObjectType(AbstractSymbol name, class_c c) {
        while (c != null) {
            SymbolTable attrs = (SymbolTable) this.objectTable.lookup(c.name);
            if (Flags.semant_debug) {
                System.err.println("Looking up object type for " + name + ": " + attrs.lookup(name));
            }
            AbstractSymbol t = (AbstractSymbol) (attrs.lookup(name));
            if (t != null) {
                return t;
            }
            c = (class_c) this.classTable.lookup(c.getParent());
        }
        return null;
    }

    public boolean isSubtype(AbstractSymbol child, AbstractSymbol parent) {
        if (child == parent) {
            return true;
        }
        if (child == TreeConstants.SELF_TYPE) {
            return false;
        }
        class_c c = (class_c) this.classTable.lookup(child);
        while (c != null) {
            if (c.getName() == parent) {
                return true;
            }
            c = (class_c) this.classTable.lookup(c.getParent());
        }
        return false;
    }

    public boolean hasClass(AbstractSymbol name) {
        return this.classTable.lookup(name) != null;
    }

    public AbstractSymbol getLCA(AbstractSymbol a, AbstractSymbol b) {
        class_c c = (class_c) this.classTable.lookup(a);
        while (c != null) {
            if (isSubtype(b, c.getName())) {
                return c.getName();
            }
            c = (class_c) this.classTable.lookup(c.getParent());
        }
        return null;
    }

    public void enterScope(class_c c) {
        SymbolTable obj = (SymbolTable) this.objectTable.lookup(c.getName());
        obj.enterScope();
    }

    public void exitScope(class_c c) {
        SymbolTable obj = (SymbolTable) this.objectTable.lookup(c.getName());
        obj.enterScope();
    }

    public void addId(class_c c, AbstractSymbol name, Object value) {
        if (Flags.semant_debug) {
            System.err.println("Adding " + name + " to " + c.getName());
        }

        SymbolTable obj = (SymbolTable) this.objectTable.lookup(c.getName());
        obj.addId(name, value);
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
