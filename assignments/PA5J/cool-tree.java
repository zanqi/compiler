// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.Enumeration;
import java.io.PrintStream;
import java.util.Vector;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

    public abstract void cgen(PrintStream s);

}

/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract AbstractSymbol getName();

    public abstract AbstractSymbol getParent();

    public abstract AbstractSymbol getFilename();

    public abstract Features getFeatures();

}

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}

/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}

/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    int index;
    int total;

    protected Formal(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}

/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }

    private AbstractSymbol type = null;

    public AbstractSymbol get_type() {
        return type;
    }

    public Expression set_type(AbstractSymbol s) {
        type = s;
        return this;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public abstract void code(
            PrintStream s,
            CgenNode cgenNode,
            CgenClassTable cgenTable,
            int tempId);

    public abstract int numTemp();
}

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}

/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void code(
            PrintStream s,
            CgenNode cgenNode,
            CgenClassTable cgenTable,
            int tempId,
            int done);

    public abstract int numTemp();

}

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}

/**
 * Defines AST constructor 'program'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class programc extends Program {
    public Classes classes;

    /**
     * Creates "program" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for classes
     */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new programc(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "program\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements();) {
            ((Class_) e.nextElement()).dump_with_types(out, n + 1);
        }
    }

    /**
     * This method is the entry point to the semantic checker. You will
     * need to complete it in programming assignment 4.
     * <p>
     * Your checker should do the following two things:
     * <ol>
     * <li>Check that the program is semantically correct
     * <li>Decorate the abstract syntax tree with type information
     * by setting the type field in each Expression node.
     * (see tree.h)
     * </ol>
     * <p>
     * You are free to first do (1) and make sure you catch all semantic
     * errors. Part (2) can be done in a second stage when you want
     * to test the complete compiler.
     */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        ClassTable classTable = new ClassTable(classes);

        /* some semantic analysis code may go here */

        if (classTable.errors()) {
            System.err.println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

    /**
     * This method is the entry point to the code generator. All of the work
     * of the code generator takes place within CgenClassTable constructor.
     * 
     * @param s the output stream
     * @see CgenClassTable
     */
    public void cgen(PrintStream s) {
        // spim wants comments to start with '#'
        s.print("# start of generated code\n");

        CgenClassTable codegen_classtable = new CgenClassTable(classes, s);

        s.print("\n# end of generated code\n");
    }

}

/**
 * Defines AST constructor 'class_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
    public AbstractSymbol name;
    public AbstractSymbol parent;
    public Features features;
    public AbstractSymbol filename;

    /**
     * Creates "class_" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for parent
     * @param a2         initial value for features
     * @param a3         initial value for filename
     */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(parent),
                (Features) features.copy(), copy_AbstractSymbol(filename));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            ((Feature) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

    public AbstractSymbol getName() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public AbstractSymbol getFilename() {
        return filename;
    }

    public Features getFeatures() {
        return features;
    }

}

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
    public AbstractSymbol name;
    public Formals formals;
    public AbstractSymbol return_type;
    public Expression expr;

    /**
     * Creates "method" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for formals
     * @param a2         initial value for return_type
     * @param a3         initial value for expr
     */
    public method(int lineNumber, AbstractSymbol a1, Formals a2, AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name), (Formals) formals.copy(),
                copy_AbstractSymbol(return_type), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n + 2, name);
        formals.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            ((Formal) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable) {
        cgenTable.enterScope();
        int i = 0;
        int numParams = formals.size();
        int numTemps = expr.numTemp();
        int numStackFields = CgenSupport.DEFAULT_OBJFIELDS + numTemps;
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); i++) {
            formalc f = (formalc) e.nextElement();
            cgenTable.addId(f.name, new CgenFormal(i, numParams));
        }

        CgenSupport.emitMethodRef(cgenNode.getName(), name, s);
        s.print(CgenSupport.LABEL);

        int stackSize = numStackFields * CgenSupport.WORD_SIZE;

        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -stackSize, s);
        CgenSupport.emitStore(CgenSupport.FP, numStackFields, CgenSupport.SP, s);
        CgenSupport.emitStore(CgenSupport.SELF, numStackFields - 1, CgenSupport.SP, s);
        CgenSupport.emitStore(CgenSupport.RA, numStackFields - 2, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4 * (numTemps + 1), s);
        CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);

        expr.code(s, cgenNode, cgenTable, 0);

        CgenSupport.emitLoad(CgenSupport.FP, numStackFields, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.SELF, numStackFields - 1, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.RA, numStackFields - 2, CgenSupport.SP, s);
        stackSize = (numStackFields + numParams) * CgenSupport.WORD_SIZE;
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, stackSize, s);
        CgenSupport.emitReturn(s);
        cgenTable.exitScope();
    }
}

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression init;

    /**
     * Creates "attr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for type_decl
     * @param a2         initial value for init
     */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }

    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl),
                (Expression) init.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

}

/**
 * Defines AST constructor 'formal'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formalc extends Formal {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;

    /**
     * Creates "formal" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for type_decl
     */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formal\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class branch extends Case {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression expr;

    /**
     * Creates "branch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for type_decl
     * @param a2         initial value for expr
     */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }

    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name), copy_AbstractSymbol(type_decl),
                (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }

    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId, int done) {
        cgenTable.enterScope();
        int nextCase = CgenSupport.labelIndex++;

        CgenNode nd = (CgenNode) cgenTable.lookup(type_decl);
        CgenSupport.emitBlti(CgenSupport.T2, nd.classTag(), nextCase, s);
        CgenSupport.emitBgti(CgenSupport.T2, nd.classTag(), nextCase, s);
        CgenTemp t = new CgenTemp(tempId);
        cgenTable.addId(name, t);
        t.emitStore(s);

        expr.code(s, cgenNode, cgenTable, tempId + 1);
        CgenSupport.emitBranch(done, s);
        CgenSupport.emitLabelDef(nextCase, s);
        cgenTable.exitScope();
    }

    @Override
    public int numTemp() {
        return expr.numTemp() + 1;
    }

}

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
    public AbstractSymbol name;
    public Expression expr;

    /**
     * Creates "assign" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     * @param a1         initial value for expr
     */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }

    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        CgenVar var = (CgenVar) cgenTable.lookup(name);
        expr.code(s, cgenNode, cgenTable, tempId);
        var.emitStore(s);
    }

    @Override
    public int numTemp() {
        return expr.numTemp();
    }

}

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol type_name;
    public AbstractSymbol name;
    public Expressions actual;

    /**
     * Creates "static_dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for expr
     * @param a1         initial value for type_name
     * @param a2         initial value for name
     * @param a3         initial value for actual
     */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2, AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }

    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(type_name),
                copy_AbstractSymbol(name), (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            Expression expr = (Expression) e.nextElement();
            expr.code(s, cgenNode, cgenTable, tempId);
            CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -CgenSupport.WORD_SIZE, s);
        }

        expr.code(s, cgenNode, cgenTable, tempId);

        int nonNullBr = CgenSupport.labelIndex++;
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nonNullBr, s);
        // Null object
        StringSymbol filename = (StringSymbol) AbstractTable.stringtable.lookup(cgenNode.getFilename().toString());
        CgenSupport.emitLoadString(CgenSupport.ACC, filename, s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal(CgenSupport.DISPATCH_ABORT, s);

        CgenSupport.emitLabelDef(nonNullBr, s);
        // load dispatch table
        CgenSupport.emitLoad(CgenSupport.T1, 2, CgenSupport.ACC, s);
        CgenNode nd = expr.get_type().equalString(TreeConstants.SELF_TYPE) ? cgenNode
                : (CgenNode) cgenTable.lookup(expr.get_type());
        CgenSupport.emitLoad(CgenSupport.T1, nd.getMethodOffset(name), CgenSupport.T1, s);
        CgenSupport.emitJalr(CgenSupport.T1, s);
    }

    @Override
    public int numTemp() {
        int nt = expr.numTemp();
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            Expression expr = (Expression) e.nextElement();
            nt = Math.max(nt, expr.numTemp());
        }
        return nt;
    }
}

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol name;
    public Expressions actual;

    /**
     * Creates "dispatch" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for expr
     * @param a1         initial value for name
     * @param a2         initial value for actual
     */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2, Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression) expr.copy(), copy_AbstractSymbol(name),
                (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            Expression expr = (Expression) e.nextElement();
            expr.code(s, cgenNode, cgenTable, tempId);
            CgenSupport.emitStore(CgenSupport.ACC, 0, CgenSupport.SP, s);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -CgenSupport.WORD_SIZE, s);
        }

        expr.code(s, cgenNode, cgenTable, tempId);

        int nonNullBr = CgenSupport.labelIndex++;
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nonNullBr, s);
        // Null object
        StringSymbol filename = (StringSymbol) AbstractTable.stringtable.lookup(cgenNode.getFilename().toString());
        CgenSupport.emitLoadString(CgenSupport.ACC, filename, s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal(CgenSupport.DISPATCH_ABORT, s);

        CgenSupport.emitLabelDef(nonNullBr, s);
        // load dispatch table
        CgenSupport.emitLoad(CgenSupport.T1, 2, CgenSupport.ACC, s);
        CgenNode nd = expr.get_type().equalString(TreeConstants.SELF_TYPE) ? cgenNode
                : (CgenNode) cgenTable.lookup(expr.get_type());
        CgenSupport.emitLoad(CgenSupport.T1, nd.getMethodOffset(name), CgenSupport.T1, s);
        CgenSupport.emitJalr(CgenSupport.T1, s);
    }

    @Override
    public int numTemp() {
        int nt = expr.numTemp();
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            Expression expr = (Expression) e.nextElement();
            nt = Math.max(nt, expr.numTemp());
        }
        return nt;
    }

}

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
    public Expression pred;
    public Expression then_exp;
    public Expression else_exp;

    /**
     * Creates "cond" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for pred
     * @param a1         initial value for then_exp
     * @param a2         initial value for else_exp
     */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    public TreeNode copy() {
        return new cond(lineNumber, (Expression) pred.copy(), (Expression) then_exp.copy(),
                (Expression) else_exp.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n + 2);
        then_exp.dump(out, n + 2);
        else_exp.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int elsebr = CgenSupport.labelIndex++;
        int endif = CgenSupport.labelIndex++;
        pred.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitLoadObjData(CgenSupport.T1, CgenSupport.ACC, s);
        CgenSupport.emitBeqz(CgenSupport.T1, elsebr, s);
        then_exp.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitBranch(endif, s);

        CgenSupport.emitLabelDef(elsebr, s);
        else_exp.code(s, cgenNode, cgenTable, tempId);

        CgenSupport.emitLabelDef(endif, s);
    }

    @Override
    public int numTemp() {
        return Math.max(Math.max(pred.numTemp(), then_exp.numTemp()), else_exp.numTemp());
    }
}

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
    public Expression pred;
    public Expression body;

    /**
     * Creates "loop" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for pred
     * @param a1         initial value for body
     */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }

    public TreeNode copy() {
        return new loop(lineNumber, (Expression) pred.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
        pred.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int trueBranch = CgenSupport.labelIndex++;
        int falseBranch = CgenSupport.labelIndex++;

        CgenSupport.emitLabelDef(trueBranch, s);
        pred.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitLoadObjData(CgenSupport.T1, CgenSupport.ACC, s);
        CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.ZERO, falseBranch, s);
        body.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitBranch(trueBranch, s);

        CgenSupport.emitLabelDef(falseBranch, s);
        CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s);
    }

    @Override
    public int numTemp() {
        return Math.max(pred.numTemp(), body.numTemp());
    }

}

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
    public Expression expr;
    public Cases cases;

    /**
     * Creates "typcase" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for expr
     * @param a1         initial value for cases
     */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }

    public TreeNode copy() {
        return new typcase(lineNumber, (Expression) expr.copy(), (Cases) cases.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n + 2);
        cases.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            ((Case) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        expr.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);
        int done = CgenSupport.labelIndex++;
        int nonNullBr = CgenSupport.labelIndex++;
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, nonNullBr, s);

        StringSymbol filename = (StringSymbol) AbstractTable.stringtable.lookup(cgenNode.getFilename().toString());
        CgenSupport.emitLoadString(CgenSupport.ACC, filename, s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal(CgenSupport.CASE_ON_NULL_ABORT, s);

        CgenSupport.emitLabelDef(nonNullBr, s);
        t.emitLoad(s, CgenSupport.T1);
        CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.T1, s);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            ((Case) e.nextElement()).code(s, cgenNode, cgenTable, tempId + 1, done);
        }
        CgenSupport.emitJal(CgenSupport.CASE_NO_MATCH_ABORT, s);
        CgenSupport.emitLabelDef(done, s);
    }

    @Override
    public int numTemp() {
        int nt = expr.numTemp();
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            nt = Math.max(((Case) e.nextElement()).numTemp() + 1, nt);
        }

        return nt;
    }

}

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
    public Expressions body;

    /**
     * Creates "block" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for body
     */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }

    public TreeNode copy() {
        return new block(lineNumber, (Expressions) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).code(s, cgenNode, cgenTable, tempId);
        }
    }

    @Override
    public int numTemp() {
        int nt = 0;
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            nt = Math.max(((Expression) e.nextElement()).numTemp(), nt);
        }
        return nt;
    }

}

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
    public AbstractSymbol identifier;
    public AbstractSymbol type_decl;
    public Expression init;
    public Expression body;

    /**
     * Creates "let" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for identifier
     * @param a1         initial value for type_decl
     * @param a2         initial value for init
     * @param a3         initial value for body
     */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2, Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier), copy_AbstractSymbol(type_decl),
                (Expression) init.copy(), (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        cgenTable.enterScope();
        CgenTemp t = new CgenTemp(tempId);
        cgenTable.addId(identifier, t);
        if (init instanceof no_expr) {
            // look for default object to store in ACC
            if (type_decl.equals(TreeConstants.Int)) {
                IntSymbol sym = (IntSymbol) AbstractTable.inttable.lookup("0");
                CgenSupport.emitLoadInt(CgenSupport.ACC, sym, s);
            } else if (type_decl.equals(TreeConstants.Str)) {
                StringSymbol sym = (StringSymbol) AbstractTable.stringtable.lookup("");
                CgenSupport.emitLoadString(CgenSupport.ACC, sym, s);
            } else if (type_decl.equals(TreeConstants.Bool)) {
                CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);
            } else {
                CgenSupport.emitLoadImm(CgenSupport.ACC, 0, s);
            }
        } else {
            if (init instanceof int_const) {
                int_const i = (int_const) init;
                IntSymbol intAddr = (IntSymbol) AbstractTable.inttable.lookup(i.token.getString());
                CgenSupport.emitLoadInt(CgenSupport.ACC, intAddr, s);
            } else {
                init.code(s, cgenNode, cgenTable, tempId);
            }
        }
        t.emitStore(s);

        body.code(s, cgenNode, cgenTable, tempId + 1);

        cgenTable.exitScope();
    }

    @Override
    public int numTemp() {
        return Math.max(init.numTemp(), body.numTemp() + 1);
    }

}

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "plus" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new plus(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);

        e2.code(s, cgenNode, cgenTable, tempId + 1);

        CgenSupport.emitJal(CgenSupport.OBJECT_COPY, s);

        CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        t.emitLoad(s, CgenSupport.T1);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.T1, s);
        CgenSupport.emitAdd(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    }

    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "sub" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new sub(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);
        e2.code(s, cgenNode, cgenTable, tempId + 1);

        CgenSupport.emitJal(CgenSupport.OBJECT_COPY, s);

        CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        t.emitLoad(s);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        CgenSupport.emitSub(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "mul" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new mul(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);
        e2.code(s, cgenNode, cgenTable, tempId + 1);

        CgenSupport.emitJal(CgenSupport.OBJECT_COPY, s);

        CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        t.emitLoad(s);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        CgenSupport.emitMul(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "divide" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new divide(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);
        e2.code(s, cgenNode, cgenTable, tempId + 1);

        CgenSupport.emitJal(CgenSupport.OBJECT_COPY, s);

        CgenSupport.emitLoad(CgenSupport.T2, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        t.emitLoad(s);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
        CgenSupport.emitDiv(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStore(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS, CgenSupport.ACC, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
    public Expression e1;

    /**
     * Creates "neg" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new neg(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitNeg(CgenSupport.ACC, CgenSupport.ACC, s);
    }

    @Override
    public int numTemp() {
        return e1.numTemp();
    }
}

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "lt" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new lt(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int done = CgenSupport.labelIndex++;
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);

        e2.code(s, cgenNode, cgenTable, tempId + 1);
        CgenSupport.emitMove(
                CgenSupport.T2,
                CgenSupport.ACC,
                s);

        t.emitLoad(s);
        CgenSupport.emitMove(
                CgenSupport.T1,
                CgenSupport.ACC,
                s);

        CgenSupport.emitLoadAddress(
                CgenSupport.ACC,
                BoolConst.truebool.ref(),
                s);
        CgenSupport.emitBlt(CgenSupport.T1, CgenSupport.T2, done, s);

        CgenSupport.emitLoadAddress(
                CgenSupport.A1,
                BoolConst.falsebool.ref(),
                s);
        CgenSupport.emitJal(CgenSupport.EQUALITY_TEST, s);
        CgenSupport.emitLabelDef(done, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "eq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new eq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int done = CgenSupport.labelIndex++;
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);

        e2.code(s, cgenNode, cgenTable, tempId + 1);
        CgenSupport.emitMove(
                CgenSupport.T2,
                CgenSupport.ACC,
                s);
        t.emitLoad(s);
        CgenSupport.emitMove(
                CgenSupport.T1,
                CgenSupport.ACC,
                s);

        CgenSupport.emitLoadAddress(
                CgenSupport.ACC,
                BoolConst.truebool.ref(),
                s);
        CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.T2, done, s);

        CgenSupport.emitLoadAddress(
                CgenSupport.A1,
                BoolConst.falsebool.ref(),
                s);
        CgenSupport.emitJal(CgenSupport.EQUALITY_TEST, s);
        CgenSupport.emitLabelDef(done, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }
}

/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
    public Expression e1;
    public Expression e2;

    /**
     * Creates "leq" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     * @param a1         initial value for e2
     */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new leq(lineNumber, (Expression) e1.copy(), (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int done = CgenSupport.labelIndex++;
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenTemp t = new CgenTemp(tempId);
        t.emitStore(s);

        e2.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitMove(
                CgenSupport.T2,
                CgenSupport.ACC,
                s);
        t.emitLoad(s);
        CgenSupport.emitMove(
                CgenSupport.T1,
                CgenSupport.ACC,
                s);

        CgenSupport.emitLoadAddress(
                CgenSupport.ACC,
                BoolConst.truebool.ref(),
                s);
        CgenSupport.emitBleq(CgenSupport.T1, CgenSupport.T2, done, s);

        CgenSupport.emitLoadAddress(
                CgenSupport.A1,
                BoolConst.falsebool.ref(),
                s);
        CgenSupport.emitJal(CgenSupport.EQUALITY_TEST, s);
        CgenSupport.emitLabelDef(done, s);
    }

    @Override
    public int numTemp() {
        return Math.max(e1.numTemp(), e2.numTemp() + 1);
    }

}

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
    public Expression e1;

    /**
     * Creates "comp" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new comp(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int done = CgenSupport.labelIndex++;
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitLoadObjData(CgenSupport.T1, CgenSupport.ACC, s);
        CgenSupport.emitTorF(CgenSupport.T1, done, s);
    }

    @Override
    public int numTemp() {
        return e1.numTemp();
    }
}

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
    public AbstractSymbol token;

    /**
     * Creates "int_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for token
     */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_AbstractSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method method is provided
     * to you as an example of code generation.
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        CgenSupport.emitLoadInt(CgenSupport.ACC,
                (IntSymbol) AbstractTable.inttable.lookup(token.getString()), s);
    }

    @Override
    public int numTemp() {
        return 0;
    }

}

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
    public Boolean val;

    /**
     * Creates "bool_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for val
     */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }

    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n + 2, val);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
        dump_Boolean(out, n + 2, val);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method method is provided
     * to you as an example of code generation.
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
    }

    @Override
    public int numTemp() {
        return 0;
    }

}

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
    public AbstractSymbol token;

    /**
     * Creates "string_const" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for token
     */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, token.getString());
        out.println("\"");
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method method is provided
     * to you as an example of code generation.
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        CgenSupport.emitLoadString(CgenSupport.ACC,
                (StringSymbol) AbstractTable.stringtable.lookup(token.getString()), s);
    }

    @Override
    public int numTemp() {
        return 0;
    }

}

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
    public AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for type_name
     */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }

    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n + 2, type_name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        CgenSupport.emitLoadProtObj(type_name, s);
        CgenSupport.emitJal(CgenSupport.OBJECT_COPY, s);
        CgenSupport.emitJal(type_name + CgenSupport.CLASSINIT_SUFFIX, s);
    }

    @Override
    public int numTemp() {
        return 0;
    }
}

/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
    public Expression e1;

    /**
     * Creates "isvoid" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for e1
     */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        int endIsVoid = CgenSupport.labelIndex++;
        e1.code(s, cgenNode, cgenTable, tempId);
        CgenSupport.emitMove(
                CgenSupport.T1,
                CgenSupport.ACC,
                s);
        CgenSupport.emitTorF(CgenSupport.T1, endIsVoid, s);
    }

    @Override
    public int numTemp() {
        return e1.numTemp();
    }
}

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {
    /**
     * Creates "no_expr" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }

    public TreeNode copy() {
        return new no_expr(lineNumber);
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
    }

    @Override
    public int numTemp() {
        return 0;
    }
}

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
    public AbstractSymbol name;

    /**
     * Creates "object" AST node.
     *
     * @param lineNumber the line in the source file from which this node came.
     * @param a0         initial value for name
     */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }

    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n + 2, name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    /**
     * Generates code for this expression. This method is to be completed
     * in programming assignment 5. (You may add or remove parameters as
     * you wish.)
     * 
     * @param s the output stream
     */
    @Override
    public void code(PrintStream s, CgenNode cgenNode, CgenClassTable cgenTable, int tempId) {
        if (name == TreeConstants.self) {
            CgenSupport.emitMove(
                    CgenSupport.ACC,
                    CgenSupport.SELF,
                    s);
            return;
        }
        CgenVar var = (CgenVar) cgenTable.lookup(name);
        var.emitLoad(s);
    }

    @Override
    public int numTemp() {
        return 0;
    }
}
