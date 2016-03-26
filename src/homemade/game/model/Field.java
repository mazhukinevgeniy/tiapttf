package homemade.game.model;

import homemade.game.Game;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by user3 on 22.03.2016.
 */
class Field
{
    //empty if value=0
    //has number if value>0
    //has special function if value<0

    private int[] cells;
    private ComboDetector comboDetector;

    private Vector<Integer> usedNumbers, availableNumbers;
    //TODO: these vectors are probably DEMOLISHING the performance, have to replace them with more suitable collections
    //the main issue is removing elements; could LinkedList be an upgrade?
    private HashSet<Integer> changedCells;

    //TODO: think if it would be nice if cells were automatically keeping track of changed verticals/horizontals


    Field()
    {
        int numberOfCells = Game.FIELD_WIDTH * Game.FIELD_HEIGHT;

        this.cells = new int[numberOfCells];

        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                this.cells[i + j * Game.FIELD_WIDTH] = Game.CELL_EMPTY;
            }

        this.comboDetector = new ComboDetector(cells);


        this.usedNumbers = new Vector<Integer>(numberOfCells);
        this.availableNumbers = new Vector<Integer>(numberOfCells);

        for (int i = 0; i < numberOfCells; i++)
        {
            this.availableNumbers.add(i + 1);
        }

        this.changedCells = new HashSet<Integer>(Game.SIMULTANEOUS_SPAWN);
        //ATM it's what would cause the maximum amount of changes
    }

    synchronized void spawnBlocks()
    {
        for (int i = 0; i < Game.FIELD_WIDTH; i++)
            for (int j = 0; j < Game.FIELD_HEIGHT; j++)
            {
                int cellCode = i + j * Game.FIELD_WIDTH;

                if (this.cells[cellCode] == Game.CELL_MARKED_FOR_SPAWN)
                {
                    this.cells[cellCode] = this.getNumber();

                    changedCells.add(cellCode);

                    System.out.println("block spawned: " + i + ", " + j + " | " + this.cells[i + j * Game.FIELD_WIDTH]);
                }
            }

        this.checkCombos();
    }

    synchronized void markCells(int targetAmount)
    {
        int cellsUsed = this.usedNumbers.size();
        //this depends on every powerup being removed at the stage of spawning blocks

        if (cellsUsed < Game.FIELD_WIDTH * Game.FIELD_HEIGHT)
        {
            Vector<Integer> freeCells = new Vector<Integer>(Game.FIELD_WIDTH * Game.FIELD_HEIGHT - cellsUsed);

            for (int i = 0; i < Game.FIELD_WIDTH; i++)
                for (int j = 0; j < Game.FIELD_HEIGHT; j++)
                {
                    if (this.cells[i + j * Game.FIELD_WIDTH] == Game.CELL_EMPTY)
                        freeCells.add(i + j * Game.FIELD_WIDTH);
                }

            for (int i = 0; i < Math.min(targetAmount, Game.FIELD_WIDTH * Game.FIELD_HEIGHT - cellsUsed); i++)
            {
                int position = (int) (Math.random() * (double) freeCells.size());

                this.cells[freeCells.get(position)] = Game.CELL_MARKED_FOR_SPAWN;

                freeCells.removeElementAt(position);
            }
        }
        else
        {
            //TODO: game over (:
        }
    }

    synchronized boolean tryMoveBlock(int cellCodeFrom, int cellCodeTo)
    {
        boolean success = false;

        int cellToValue = cells[cellCodeTo], cellFromValue = cells[cellCodeFrom];

        if (cellToValue <= 0 && cellFromValue > 0)
        {
            cells[cellCodeTo] = cellFromValue;
            cells[cellCodeFrom] = Game.CELL_EMPTY;

            success = true;

            changedCells.add(cellCodeTo);
            checkCombos();
        }

        return success;
    }

    private int getNumber()
    {
        int length = this.availableNumbers.size();
        int position = (int)(Math.random() * (double)length);
        int number = this.availableNumbers.get(position);

        this.usedNumbers.add(number);
        this.availableNumbers.removeElementAt(position);

        return number;
    }

    private void checkCombos()
    {
        if (Game.AUTOCOMPLETION)
        {
            Set<Integer> cellsToRemove = comboDetector.findCellsToRemove(changedCells);

            for (int cell : cellsToRemove)
            {
                int value = cells[cell];

                cells[cell] = Game.CELL_EMPTY;
                usedNumbers.removeElementAt(usedNumbers.indexOf(value));
                availableNumbers.add(value);
            }

            changedCells.clear();
        }
    }

    synchronized int [] cloneToArray()
    {
        return this.cells.clone();
    }
}