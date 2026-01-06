package edu.kis.powp.jobs2d.events;

import edu.kis.powp.jobs2d.command.CompoundCommand;
import edu.kis.powp.jobs2d.command.DriverCommand;
import edu.kis.powp.jobs2d.command.OperateToCommand;
import edu.kis.powp.jobs2d.command.SetPositionCommand;
import edu.kis.powp.jobs2d.drivers.DriverManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelectTestCompoundCommandOptionListener implements ActionListener {

    private DriverManager driverManager;

    public SelectTestCompoundCommandOptionListener(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int baseX = -50;
        int baseY = -50;
        int squareSize = 100;
        int roofHeight = 60;

        List<DriverCommand> squareCommands = new ArrayList<>();
        squareCommands.add(new SetPositionCommand(baseX, baseY));
        squareCommands.add(new OperateToCommand(baseX + squareSize, baseY));
        squareCommands.add(new OperateToCommand(baseX + squareSize, baseY + squareSize));
        squareCommands.add(new OperateToCommand(baseX, baseY + squareSize));
        squareCommands.add(new OperateToCommand(baseX, baseY));

        CompoundCommand square = CompoundCommand.builder().add(squareCommands).build();

        List<DriverCommand> roofCommands = new ArrayList<>();
        roofCommands.add(new SetPositionCommand(baseX, -(baseY + squareSize)));
        roofCommands.add(new OperateToCommand(baseX + squareSize / 2, -(baseY + squareSize + roofHeight)));
        roofCommands.add(new OperateToCommand(baseX + squareSize, -(baseY + squareSize)));

        CompoundCommand house = CompoundCommand.builder()
                        .add(square) // adds other build (base)
                        .add(roofCommands) // adds new list of commands (roof)
                        .build(); // returns new immutable object (base+roof)

        house.execute(driverManager.getCurrentDriver());
    }
}
