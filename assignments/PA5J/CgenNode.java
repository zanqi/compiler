/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
*/

// This is a project skeleton file

import java.io.PrintStream;
import java.util.Vector;
import java.util.Collections;
import java.util.Enumeration;

class CgenNode extends class_c {
    /** The parent of this node in the inheritance tree */
    private CgenNode parent;

    /** The children of this node in the inheritance tree */
    private Vector children;

    /** Indicates a basic class */
    final static int Basic = 0;

    /** Indicates a class that came from a Cool program */
    final static int NotBasic = 1;

    /** Does this node correspond to a basic class? */
    private int basic_status;

    /**
     * Constructs a new CgenNode to represent class "c".
     * 
     * @param c            the class
     * @param basic_status is this class basic or not
     * @param table        the class table
     */
    CgenNode(Class_ c, int basic_status, CgenClassTable table) {
        super(0, c.getName(), c.getParent(), c.getFeatures(), c.getFilename());
        this.parent = null;
        this.children = new Vector();
        this.basic_status = basic_status;
        AbstractTable.stringtable.addString(name.getString());
    }

    void addChild(CgenNode child) {
        children.addElement(child);
    }

    /**
     * Gets the children of this class
     * 
     * @return the children
     */
    Enumeration getChildren() {
        return children.elements();
    }

    /**
     * Sets the parent of this class.
     * 
     * @param parent the parent
     */
    void setParentNd(CgenNode parent) {
        if (this.parent != null) {
            Utilities.fatalError("parent already set in CgenNode.setParent()");
        }
        if (parent == null) {
            Utilities.fatalError("null parent in CgenNode.setParent()");
        }
        this.parent = parent;
    }

    /**
     * Gets the parent of this class
     * 
     * @return the parent
     */
    CgenNode getParentNd() {
        return parent;
    }

    /**
     * Returns true is this is a basic class.
     * 
     * @return true or false
     */
    boolean basic() {
        return basic_status == Basic;
    }

    void codeDispatchTable(PrintStream str) {
        if (getParentNd() != null) {
            getParentNd().codeDispatchTable(str);
        }
        for (Enumeration e = getFeatures().getElements(); e.hasMoreElements();) {
            Feature f = (Feature) e.nextElement();
            if (f instanceof method) {
                method m = (method) f;
                str.println(CgenSupport.WORD + name.getString() + "." + m.name.getString());
            }
        }
    }

    void codeProtObj(PrintStream s, int classTag) {
        s.println(CgenSupport.WORD + "-1");
        s.print(name.getString() + CgenSupport.PROTOBJ_SUFFIX + CgenSupport.LABEL);
        s.println(CgenSupport.WORD + classTag);
        int numAttrs = 0;
        for (Enumeration e = getFeatures().getElements(); e.hasMoreElements();) {
            Feature f = (Feature) e.nextElement();
            if (f instanceof attr) {
                numAttrs++;
            }
        }
        s.println(CgenSupport.WORD + (CgenSupport.DEFAULT_OBJFIELDS + numAttrs));

        s.println(CgenSupport.WORD + name.getString() + CgenSupport.DISPTAB_SUFFIX); // dispatch table
        for (Enumeration e = getFeatures().getElements(); e.hasMoreElements();) {
            Feature f = (Feature) e.nextElement();
            if (f instanceof attr) {
                attr a = (attr) f;
                if (a.type_decl.equals(TreeConstants.Int)) {
                    IntSymbol intSymbol = (IntSymbol) AbstractTable.inttable.addInt(0);
                    s.print(CgenSupport.WORD);
                    intSymbol.codeRef(s);
                    s.println("");
                } else if (a.type_decl.equals(TreeConstants.Str)) {
                    StringSymbol stringSymbol = (StringSymbol) AbstractTable.stringtable.addString("");
                    s.print(CgenSupport.WORD);
                    stringSymbol.codeRef(s);
                    s.println("");
                } else if (a.type_decl.equals(TreeConstants.Bool)) {
                    s.print(CgenSupport.WORD);
                    BoolConst.falsebool.codeRef(s);
                    s.println("");
                } else {
                    s.println(CgenSupport.WORD + "0");
                }
            }
        }
    }

    void codeObjInit(PrintStream s, CgenClassTable cgenTable) {
        cgenTable.enterScope();
        s.print(CgenSupport.objInitRef(name) + CgenSupport.LABEL);
        // todo: obj should not be fixed at size 12
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -12, s);
        CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, s);
        CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, s);
        CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP, 4, s);
        CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, s);
        if (getParentNd() != null && !getParentNd().name.equals(TreeConstants.No_class)) {
            s.println(CgenSupport.JAL + CgenSupport.objInitRef(getParentNd().name));
        }
        int offset = 0;
        for (Enumeration e = getFeatures().getElements(); e.hasMoreElements();) {
            Feature f = (Feature) e.nextElement();
            if (f instanceof attr) {
                attr a = (attr) f;
                if (a.init instanceof no_expr) {
                    continue;
                }
                a.init.code(s, this, cgenTable, 0);
                CgenSupport.emitStore(CgenSupport.ACC, CgenSupport.DEFAULT_OBJFIELDS + offset++, CgenSupport.SELF, s);
            }
        }
        CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
        CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, s);
        CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, s);
        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, 12, s);
        CgenSupport.emitReturn(s);
        cgenTable.exitScope();
    }

    void codeMethods(PrintStream s, CgenClassTable cgenTable) {
        cgenTable.enterScope();
        for (attr a : getAttrs()) {
            cgenTable.addId(a.name, new CgenAttr(a.name, this));
        }
        for (Enumeration e = getFeatures().getElements(); e.hasMoreElements();) {
            Feature f = (Feature) e.nextElement();
            if (f instanceof method) {
                method m = (method) f;
                m.code(s, this, cgenTable);
            }
        }
        cgenTable.exitScope();
    }

    // int getMethodOffset(String methodName) {
    // int offset = 0;
    // Vector<CgenNode> nodes = new Vector<CgenNode>();
    // for (CgenNode c = this; c != null; c = c.getParentNd()) {
    // nodes.add(c);
    // }
    // for (int i = nodes.size() - 1; i >= 0; i--) {
    // CgenNode c = nodes.get(i);
    // for (Enumeration e = c.getFeatures().getElements(); e.hasMoreElements();) {
    // Feature f = (Feature) e.nextElement();
    // if (f instanceof method) {
    // method m = (method) f;
    // if (m.name.getString().equals(methodName)) {
    // return offset;
    // }
    // offset++;
    // }
    // }
    // }
    // return -1;
    // }

    Vector<CgenNode> getLineage() {
        Vector<CgenNode> nodes = new Vector<CgenNode>();
        for (CgenNode c = this; c != null; c = c.getParentNd()) {
            nodes.add(c);
        }
        Collections.reverse(nodes);
        return nodes;
    }

    Vector<attr> getAttrs() {
        Vector<attr> attrs = new Vector<attr>();
        for (CgenNode c : getLineage()) {
            for (Enumeration e = c.getFeatures().getElements(); e.hasMoreElements();) {
                Feature f = (Feature) e.nextElement();
                if (f instanceof attr) {
                    attr a = (attr) f;
                    attrs.add(a);
                }
            }
        }
        return attrs;
    }

    int getAttrOffset(AbstractSymbol attrName) {
        int offset = 0;
        for (attr a : getAttrs()) {
            if (a.name.equalString(attrName)) {
                return offset;
            }
            offset++;
        }
        throw new IllegalStateException("Attribute " + attrName + " is not found in class " + name);
    }

    Vector<method> getMethods() {
        Vector<method> attrs = new Vector<method>();
        for (CgenNode c : getLineage()) {
            for (Enumeration e = c.getFeatures().getElements(); e.hasMoreElements();) {
                Feature f = (Feature) e.nextElement();
                if (f instanceof method) {
                    attrs.add((method) f);
                }
            }
        }
        return attrs;
    }

    int getMethodOffset(AbstractSymbol methodName) {
        int offset = 0;
        for (method m : getMethods()) {
            if (m.name.equalString(methodName)) {
                return offset;
            }
            offset++;
        }
        throw new IllegalStateException("Method " + methodName + " is not found in class " + name);
    }
}

abstract class CgenVar {
    public abstract void emitStore(PrintStream s);

    public abstract void emitLoad(PrintStream s);
}

class CgenTemp extends CgenVar {
    int id;

    public CgenTemp(int id) {
        this.id = id;
    }

    @Override
    public void emitStore(PrintStream s) {
        CgenSupport.emitStore(
                CgenSupport.ACC,
                -id - 1,
                CgenSupport.FP,
                s);
    }

    @Override
    public void emitLoad(PrintStream s) {
        CgenSupport.emitLoad(
                CgenSupport.ACC,
                -id - 1,
                CgenSupport.FP,
                s);
    }

    public void emitLoad(PrintStream s, String dest) {
        CgenSupport.emitLoad(
                dest,
                -id - 1,
                CgenSupport.FP,
                s);
    }
}

class CgenFormal extends CgenVar {
    int id;
    int total;

    public CgenFormal(int id, int total) {
        this.id = id;
        this.total = total;
    }

    public void emitStore(PrintStream s) {
        CgenSupport.emitStore(
                CgenSupport.ACC,
                total - id - 1 + CgenSupport.DEFAULT_OBJFIELDS,
                CgenSupport.FP,
                s);
    }

    public void emitLoad(PrintStream s) {
        CgenSupport.emitLoad(
                CgenSupport.ACC,
                total - id - 1 + CgenSupport.DEFAULT_OBJFIELDS,
                CgenSupport.FP,
                s);
    }
}

class CgenAttr extends CgenVar {
    AbstractSymbol name;
    CgenNode node;

    public CgenAttr(AbstractSymbol name, CgenNode node) {
        this.name = name;
        this.node = node;
    }

    public void emitStore(PrintStream s) {
        int offset = node.getAttrOffset(name) + CgenSupport.DEFAULT_OBJFIELDS;
        CgenSupport.emitStore(
                CgenSupport.ACC,
                offset,
                CgenSupport.SELF,
                s);
    }

    public void emitLoad(PrintStream s) {
        int offset = node.getAttrOffset(name) + CgenSupport.DEFAULT_OBJFIELDS;
        CgenSupport.emitLoad(
                CgenSupport.ACC,
                offset,
                CgenSupport.SELF,
                s);
    }
}