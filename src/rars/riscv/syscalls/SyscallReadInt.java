package rars.riscv.syscalls;

import rars.*;
import rars.riscv.AbstractSyscall;
import rars.riscv.hardware.RegisterFile;
import rars.util.SystemIO;

public class SyscallReadInt extends AbstractSyscall {
    public SyscallReadInt() {
        super("ReadInt", "Reads an int from input console", "N/A", "a0 = the int");
    }

    public void simulate(ProgramStatement statement) throws SimulationException {
        try {
            RegisterFile.updateRegister("a0", SystemIO.readInteger(this.getNumber()));
        } catch (NumberFormatException e) {
            if (e.getMessage().equals("Cancel"))
                throw new CancelException();
            throw new ExitingException(statement,
                    "invalid integer input (syscall " + this.getNumber() + ")");
        }
    }

}