package edu.kis.powp.jobs2d.command.gui;

import edu.kis.powp.jobs2d.command.CompoundCommand;
import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.ICompoundCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;
import edu.kis.powp.jobs2d.visitor.CommandVisitor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Iterator;

public class CommandToGuiNodeVisitor implements CommandVisitor {
    private final DefaultMutableTreeNode parent;

    public CommandToGuiNodeVisitor(DefaultMutableTreeNode parent) {
        this.parent = parent;
    }

    @Override
    public void visit(SetPositionCommand command) {
        parent.add(new DefaultMutableTreeNode(new GuiAtomicCommandRepresentation(command)));
    }

    @Override
    public void visit(OperateToCommand command) {
        parent.add(new DefaultMutableTreeNode(new GuiAtomicCommandRepresentation(command)));
    }

    @Override
    public void visit(ICompoundCommand command) {
        String name = "Compound Command";
        if (command instanceof CompoundCommand) {
            name = ((CompoundCommand) command).getName();
        }
        DefaultMutableTreeNode compositeNode = new DefaultMutableTreeNode(new GuiCompositeCommandRepresentation(name));
        parent.add(compositeNode);

        Iterator<DriverCommand> iterator = command.iterator();
        CommandToGuiNodeVisitor visitor = new CommandToGuiNodeVisitor(compositeNode);
        while (iterator.hasNext()) {
            iterator.next().accept(visitor);
        }
    }
}
