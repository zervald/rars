package rars.venus.run;

import rars.Globals;
import rars.Settings;
import rars.SimulationException;
import rars.riscv.hardware.RegisterFile;
import rars.simulator.ProgramArgumentList;
import rars.simulator.Simulator;
import rars.simulator.SimulatorNotice;
import rars.venus.ExecutePane;
import rars.venus.FileStatus;
import rars.venus.GuiAction;
import rars.venus.VenusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */

/**
 * Action  for the Run -> Step menu item
 */
public class RunStepAction extends GuiAction {

    private VenusUI mainUI;

    public RunStepAction(String name, Icon icon, String descrip,
                         Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel);
        mainUI = gui;
    }

    /**
     * perform next simulated instruction step.
     */
    public void actionPerformed(ActionEvent e) {
        String name = this.getValue(Action.NAME).toString();
        if (!mainUI.onStartedSimulation(name)) {
            return;
        }

        // Setup callback for after step finishes
        final Observer stopListener =
                new Observer() {
                    public void update(Observable o, Object simulator) {
                        SimulatorNotice notice = ((SimulatorNotice) simulator);
                        if (notice.getAction() != SimulatorNotice.SIMULATOR_STOP) return;
                        EventQueue.invokeLater(() -> mainUI.onStoppedSimulation(name, notice));
                        o.deleteObserver(this);
                    }
                };
        Simulator.getInstance().addObserver(stopListener);
        Globals.program.startSimulation(1, null);
    }
}
