package homemade.game.model.combo;

import homemade.game.Combo;
import homemade.game.ComboEffect;
import homemade.game.controller.BlockEventHandler;
import homemade.game.fieldstructure.CellCode;
import homemade.game.fieldstructure.Direction;
import homemade.game.fieldstructure.FieldStructure;
import homemade.game.fieldstructure.LinkCode;
import homemade.game.model.GameModelLinker;
import homemade.game.model.cellmap.CellMapReader;

import java.util.HashSet;
import java.util.Set;

public class ComboDetector
{
    private FieldStructure structure;
    private CellMapReader cellMap;
    private BlockEventHandler blockEventHandler;

    private int minCombo;

    public ComboDetector(GameModelLinker linker, BlockEventHandler blockEventHandler)
    {
        structure = linker.getStructure();
        cellMap = linker.getMapReader();

        this.blockEventHandler = blockEventHandler;

        minCombo = linker.getSettings().minCombo();
    }

    public ComboPack findCombos(Set<CellCode> starts)
    {
        int numberOfStarts = starts.size();

        Set<Integer> horizontals = new HashSet<Integer>(numberOfStarts);
        Set<Integer> verticals = new HashSet<Integer>(numberOfStarts);

        for (CellCode cellCode : starts)
        {
            horizontals.add(cellCode.y());
            verticals.add(cellCode.x());
        }

        ComboPack pack = new ComboPack();

        for (int horizontal : horizontals)
        {
            iterateThroughTheLine(pack, structure.getCellCode(0, horizontal), Direction.RIGHT);
        }

        for (int vertical : verticals)
        {
            iterateThroughTheLine(pack, structure.getCellCode(vertical, 0), Direction.BOTTOM);
        }

        return pack;
    }

    /**
     * @param start cellCode of the beginning
     * @param mainDirection where to look for the next cell
     */
    private void iterateThroughTheLine(ComboPack pack, CellCode start, Direction mainDirection)
    {
        CellCode currentCell = start;

        while (currentCell != null)
        {
            CellCode nextCell = currentCell.neighbour(mainDirection);

            if (nextCell != null)
            {
                LinkCode nextLink = structure.getLinkCode(currentCell, nextCell);
                int comboLength = cellMap.getChainLength(nextLink);

                if (comboLength >= minCombo)
                {
                    Set<CellCode> comboCells = new HashSet<>();
                    int comboTier = comboLength - minCombo + 1;

                    CellCode lastCell = currentCell;

                    for (int i = 0; i < comboLength; i++)
                    {
                        currentCell = lastCell;
                        nextCell = lastCell.neighbour(mainDirection);

                        ComboEffect comboEffect = cellMap.getCell(currentCell).effect();

                        if (comboEffect != null)
                        {
                            comboTier += comboEffect.tierBonus();
                            pack.addMultiplier(comboEffect.multiplierBonus());

                            if (comboEffect == ComboEffect.EXPLOSION)
                            {
                                Set<CellCode> vicinity = currentCell.getVicinity();
                                comboCells.addAll(vicinity);

                                for (CellCode cell : vicinity)
                                    if (cellMap.getCell(cell).isNormalBlock())
                                        blockEventHandler.blockRemoved(cell);

                                blockEventHandler.blockExploded(currentCell);
                            }
                        }

                        comboCells.add(currentCell);
                        blockEventHandler.blockRemoved(currentCell);

                        lastCell = nextCell;
                    }

                    System.out.println("length " + comboLength + " tier " + comboTier);
                    pack.add(new Combo(comboCells, comboTier));

                    //currentCell must be the last cell of combo after this block
                }
                else
                {
                    currentCell = nextCell;
                }
            }
            else
            {
                currentCell = null;
            }
        }

    }
}