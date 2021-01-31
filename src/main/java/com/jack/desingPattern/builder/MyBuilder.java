package com.jack.desingPattern.builder;

/**
 * 构建者模式
 *      主要点：
 *          作用于复杂对象的构建上，使用者可以根据自己的需求生产不同的对象(工厂模式是把复杂的构建过程封装，使用者不关心构建过程，
 *          每次获取的对象都是具有相似属性的对象，而构建者模式，可以封装部分固定内容，其余的可由使用者指定)
 *
 *      参考java中的StringBuilder
 */
public class MyBuilder {


    public static void main(String[] args) {
        ComputorBulider bulider = new MacComputorBuilder();
        bulider.setCpu("inter").setMemory("8G").build();
    }
}

class Computer {
    private String cpu;
    private String board;
    private String memory;
    private String display;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

abstract class ComputorBulider{
    abstract ComputorBulider setCpu(String cpu);
    abstract ComputorBulider setBoard(String board);
    abstract ComputorBulider setMemory(String memory);
    abstract ComputorBulider setDisplay(String display);
    abstract Computer build();
}


class MacComputorBuilder extends ComputorBulider {

    private Computer computer;

    public MacComputorBuilder() {
        computer = new Computer();
    }


    @Override
    ComputorBulider setCpu(String cpu) {
        computer.setCpu(cpu);
        return this;
    }

    @Override
    ComputorBulider setBoard(String board) {
        computer.setBoard(board);
        return this;
    }

    @Override
    ComputorBulider setMemory(String memory) {
        computer.setMemory(memory);
        return this;
    }

    @Override
    ComputorBulider setDisplay(String display) {
        computer.setDisplay(display);
        return this;
    }

    @Override
    Computer build() {
        return computer;
    }


}