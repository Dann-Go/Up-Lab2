import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.lang.reflect.InvocationTargetException;

class Sign implements GraphSample {
    static final int WIDTH = 1500, HEIGHT = 500;
    public String getName() { return "Paints"; }
    public int getWidth() { return WIDTH; }
    public int getHeight() { return HEIGHT; }

    public void draw(Graphics2D ig, Component c) {

        BufferedImage bimage = new BufferedImage(WIDTH/2, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setPaint(new GradientPaint(0, HEIGHT, new Color(200, 200, 200), 0, 0,
                new Color(100, 100, 100)));
        g.fillRect(0, 0, WIDTH/2, HEIGHT);

        g.setColor(new Color(0, 0, 255));
        g.setStroke(new BasicStroke(40));
        g.drawRect(50, 50, WIDTH/2 - 100, HEIGHT - 100);

        g.setColor(Color.white);
        g.fillRect(70, 70, WIDTH/2 - 140, HEIGHT - 140);

        Font font = new Font("Serif", Font.BOLD, 10);
        Font bigfont = font.deriveFont(AffineTransform.getScaleInstance(30.0, 30.0));
        GlyphVector gv = bigfont.createGlyphVector(g.getFontRenderContext(),
                "GO");
        Shape shapeD = gv.getGlyphOutline(0);
        Shape shapeA = gv.getGlyphOutline(1);

        g.setStroke(new BasicStroke(5.0f));

        Paint shadowPaint = new Color(0, 0, 0, 100);
        AffineTransform shadowTransform = AffineTransform.getShearInstance(-1.0, 0.0);
        shadowTransform.scale(1, 0.5);

        g.translate(140, 345);
        g.setPaint(shadowPaint);
        g.translate(0, 20);
        g.fill(shadowTransform.createTransformedShape(shapeD));
        g.fill(shadowTransform.createTransformedShape(shapeA));
        g.translate(0, -20);
        g.setPaint(new Color(0, 0, 255));
        g.fill(shapeD);
        g.fill(shapeA);

        ig.drawImage(bimage, 0,0,c);
        ig.drawString("No filters", 10 , 13);
        ig.drawImage(new RescaleOp(1.25f, 0, null).filter
                (bimage, null), WIDTH/2,0, c);
        ig.drawString("Image Brighten", WIDTH - 100,13);
        ig.drawLine(WIDTH/2, 0,WIDTH/2, HEIGHT);
    }
}

class Main extends JFrame {

    static final String classname = Sign.class.getName();
    public Main(final GraphSample[] examples) {
        super("Лаба 2 вар 11");

        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        final JTabbedPane tpane = new JTabbedPane();
        cpane.add(tpane, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        for (GraphSample e : examples) {
            tpane.addTab(e.getName(), new GraphSamplePane(e));
        }
    }

    public static class GraphSamplePane extends JComponent {
        GraphSample example;
        Dimension size;

        public GraphSamplePane(GraphSample example) {
            this.example = example;
            size = new Dimension(example.getWidth(), example.getHeight());
            setMaximumSize( size );
        }

        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, size.width, size.height);
            g.setColor(Color.black);
            example.draw((Graphics2D) g, this);
        }

        public Dimension getPreferredSize() { return size; }
        public Dimension getMinimumSize() { return size; }
    }

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        GraphSample[] examples = new GraphSample[1];

        Class exampleClass = Sign.class;
        examples[0] = (GraphSample) exampleClass.getDeclaredConstructor().newInstance();

        Main f = new Main(examples);
        f.pack();
        f.setVisible(true);
    }
}

interface GraphSample {
    String getName();
    int getWidth();
    int getHeight();
    void draw(Graphics2D g, Component c);
}