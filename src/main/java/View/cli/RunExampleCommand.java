package View.cli;

import Controller.Controller;
import Exceptions.InterpreterException;

import java.util.Objects;
import java.util.Scanner;

public class RunExampleCommand extends Command{
    private final Controller controller;

    public RunExampleCommand(String key, String description, Controller controller){
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try{
            controller.allStep();
        }catch(InterpreterException exception){
            System.out.println(exception.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}