package com.withing.datastructure;

import lombok.Data;

import java.util.Objects;

/**
 * @author huangweixin7
 * @date 2020/3/30 13:43:07
 * description: RedBlackTree
 */
@Data
public class RedBlackTree {
    /**
     * 权值
     */
    int value;
    /**
     * 是否红节点
     */
    boolean red;
    /**
     * 父节点
     */
    RedBlackTree parent;
    /**
     * 左孩子
     */
    RedBlackTree lChild;
    /**
     * 右孩子
     */
    RedBlackTree rChild;

    public RedBlackTree() {
        red = false;
        value = 0;
        parent = null;
        lChild = null;
        rChild = null;
    }

    public RedBlackTree(int value) {
        red = false;
        this.value = value;
        parent = null;
        lChild = null;
        rChild = null;
    }

    //1.已存在树root，插入v节点
    //2.寻找所需插入位置的父节点p,记录插入的位置左节点pl或者右节点pr，记录祖父节点pp，记录p的兄弟节点s
    //3.如果插入的父节点p为黑色，则新建节点v为红色，直接插入，或者无父节点，则直接把节点至黑，插入
    //4.如果插入的父节点为红色，则看兄弟节点是否为红色。
    //4.1若兄弟节点s为红色，则把父节点p和兄弟节点s染为黑色，祖父节点为红色，继续遍历祖父节点。
    //4.2 若兄弟节点为黑色，则分四种情况.
    //4.2.1插入位置为p的左孩子。且p是左孩子。则把父节点p变为黑色，祖父节点变为红色，然后把祖父节点右旋
    //4.2.2插入位置为p的右孩子，且p是左孩子。则先把p左旋，转化成4.1的情况，然后再往下执行。
    //4.2.3插入位置为p的右孩子，且p是右孩子。则把父节点p变为黑色，祖父节点变为红色，然后把祖父节点左旋。
    //4.2.4插入位置为p的左孩子，且p是右孩子。则把p右旋，转化成4.2.4情形。
    public static RedBlackTree insertNode(int value, RedBlackTree root) {
        if (Objects.isNull(root)) {
            return new RedBlackTree(value);
        }
        RedBlackTree p = root;
        RedBlackTree x;
        while (true) {
            if (value < p.getValue()) {
                if (p.lChild == null) {
                    x = p.lChild = new RedBlackTree(value);
                    x.parent = p;
                    x.setRed(true);
                    break;
                } else {
                    p = p.lChild;
                }
            } else {
                if (p.rChild == null) {
                    x = p.rChild = new RedBlackTree(value);
                    x.parent = p;
                    x.setRed(true);
                    break;
                } else {
                    p = p.rChild;
                }
            }
        }
        root = balanceTree(root, x);
        return root;
    }

    public static RedBlackTree balanceTree(RedBlackTree root, RedBlackTree x) {
        if (Objects.isNull(x)) {
            return root;
        }
        RedBlackTree p;
        RedBlackTree pp, s;
        do {
            if (Objects.isNull(p = x.parent)) {
                x.red = false;
                return x;
            } else if (!p.red || Objects.isNull(pp = p.parent)) {
                return root;
            }
            if (p == pp.lChild) {
                if (Objects.nonNull(s = pp.rChild) && s.red) {
                    p.red = false;
                    s.red = false;
                    pp.red = true;
                    x = pp;
                } else {
                    if (x == p.rChild) {
                        root = rotateLeft(root, x);
                    }
                    p.red = false;
                    pp.red = true;
                    root = rotateRight(root, pp);
                }
            } else {
                if (Objects.nonNull(s = pp.lChild) && s.red) {
                    p.red = false;
                    s.red = false;
                    pp.red = true;
                    x = pp;
                } else {
                    if (x == p.lChild) {
                        x = root = rotateRight(root, p);
                    }
                    p.red = false;
                    pp.red = true;
                    root = rotateLeft(root, pp);
                }
            }
        } while (true);
    }

    public static RedBlackTree rotateLeft(RedBlackTree root, RedBlackTree p) {
        RedBlackTree pp, pr, rl;
        if (Objects.nonNull(p) && Objects.nonNull(pr = p.rChild)) {
            if (Objects.nonNull(rl = pr.lChild)) {
                rl.parent = p;
            }
            if (Objects.isNull(pp = pr.parent = p.parent)) {
                (root = pr).red = false;
            } else if (pp.lChild == p) {
                pp.lChild = pr;
            } else {
                pp.rChild = pr;
            }
            p.parent = pr;
            p.rChild = rl;
            pr.lChild = p;
        }
        return root;
    }

    public static RedBlackTree rotateRight(RedBlackTree root, RedBlackTree p) {
        RedBlackTree pp, pl, lr;
        if (Objects.nonNull(p) && Objects.nonNull(pl = p.lChild)) {
            if (Objects.nonNull(lr = pl.rChild)) {
                lr.parent = p;
            }
            if (Objects.isNull(pp = pl.parent = p.parent)) {
                root = pl;
                pl.red = false;
            } else if (pp.lChild == p) {
                pp.lChild = pl;
            } else {
                pp.rChild = pl;
            }
            p.parent = pl;
            pl.rChild = p;
            p.lChild = lr;
        }
        return root;
    }

    public static void main(String[] args) {
        RedBlackTree root = insertNode(80, null);
        root = insertNode(120, root);
        root = insertNode(10, root);
        root = insertNode(20, root);
        root = insertNode(60, root);
        root = insertNode(50, root);
        root = insertNode(100, root);
        root = insertNode(90, root);
        root = insertNode(140, root);
        root = insertNode(30, root);
        root.printfTree(1);
        RedBlackTree find = find(50, root);
        System.out.println(find);
    }

    public static RedBlackTree find(int value, RedBlackTree p) {
        if (value == p.getValue()) {
            return p;
        }
        do {
            if (value < p.getValue()) {
                p = p.lChild;
            } else if (value > p.getValue()) {
                p = p.rChild;
            } else {
                return p;
            }
        } while (Objects.nonNull(p));
        return p;
    }

    /**
     * 删除节点重要操作，找到删除节点的前驱节点或后继节点，替代被删除节点
     * 前驱节点为左子树的最右节点，后继节点为右子树的最左节点
     * 删除节点有以下几种情况
     * 1.无子树
     * 2.仅有左子树
     * 3.仅有右子树
     * 4.左右子树均有
     */
    public static RedBlackTree remove(int value, RedBlackTree p) {

    }

    public static RedBlackTree root(RedBlackTree node) {
        if (Objects.isNull(node)) {
            return null;
        }
        while (Objects.nonNull(node.parent)) {
            node = node.parent;
        }
        return node;
    }

    @Override
    public String toString() {
        return this.value + "-" + this.red;
    }

    public void printfTree(int i) {
        System.out.println(i + ":" + this.value + "-" + this.red);
        if (Objects.nonNull(this.lChild)) {
            this.lChild.printfTree(i + 1);
        }
        if (Objects.nonNull(this.rChild)) {
            this.rChild.printfTree(i + 1);
        }
    }

}
