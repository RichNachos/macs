import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JBrainTetris extends JTetris{
    protected DefaultBrain brain;
    protected Brain.Move bestMove;
    protected int oldCount;
    protected JCheckBox brainMode;
    protected JPanel little;
    protected JSlider adversary;
    protected JLabel status;
    public JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
    }

    @Override
    public void tick(int verb) {
        if (this.count != this.oldCount) {
            board.undo();
            bestMove = brain.bestMove(this.board, this.currentPiece, this.board.getHeight() - TOP_SPACE, null);
            this.oldCount = this.count;
        }

        if (verb == JTetris.DOWN && this.brainMode.isSelected() && bestMove != null) {
            if (this.currentX > bestMove.x) {
                super.tick(JTetris.LEFT);
            }
            if (this.currentX < bestMove.x) {
                super.tick(JTetris.RIGHT);
            }
            if (!bestMove.piece.equals(this.currentPiece)) {
                super.tick(JTetris.ROTATE);
            }
        }
        super.tick(verb);
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel)super.createControlPanel();

        // BRAIN CHECKBOX
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);

        // ADVERSARY SLIDER
        // make a little panel, put a JSlider in it. JSlider responds to getValue()
        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary);
        status = new JLabel("ok");
        little.add(status);
        // now add little to panel of controls
        panel.add(little);

        return panel;
    }

    @Override
    public Piece pickNextPiece() {
        int rnd = random.nextInt(98) + 1;
        int sld = adversary.getValue();

        if (rnd >= sld) { // pick random piece
            status.setText("ok");
            return super.pickNextPiece();
        } else { // pick worst piece
            status.setText("*ok*");
            Brain.Move[] piecesMoves = new Brain.Move[pieces.length];
            int ind = 0;
            for (int i = 0; i < pieces.length; i++) {
                board.undo();
                piecesMoves[i] = brain.bestMove(board, pieces[i], board.getHeight(), null);
                if (piecesMoves[i].score > piecesMoves[ind].score) {
                    ind = i;
                }
            }
            return piecesMoves[ind].piece;
        }

    }

    public static void main(String[] args) {
        // Set GUI Look And Feel Boilerplate.
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }
}
