package autotetris;

import javax.swing.UIManager;

import autotetris.model.Model;
import autotetris.model.TetrisSolver;
import autotetris.model.Trainer;
import autotetris.view.Application;

public class AutoTetris {

	private static void runGUI() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		Model m = new Model();
		final Application app = new Application(m);

		app.bindExecutionController(new autotetris.controller.ExecutionController(m, app));
		app.setVisible(true);
	}

	public static void main(String[] args) {
	    if (args.length > 5 && args[0].equals("masstrain")) {
        	int P, M, T, I, G;
        	try {
				P = Integer.parseInt(args[1]);
				G = Integer.parseInt(args[2]);
				M = Integer.parseInt(args[3]);
				T = Integer.parseInt(args[4]);
				I = Integer.parseInt(args[5]);

				Trainer t = new Trainer(P, G, M, I);
				t.train(T);
				return;
			} catch (Exception e) {
                System.out.println("One of parameters P M T I G  invalid");
            }

        }
	    else if(args.length == 0) {
	        runGUI();
	        return;
		}
        else {
			System.out.println("Invoke as either:");
			System.out.println("./AutoTetris (or whatever you named the exe");
			System.out.println("./AutoTetris masstrain <P> <G> <M> <T> <I>");
			System.out.println("Where: ");
			System.out.println("P = population size");
			System.out.println("G = new offspring per round");
			System.out.println("M = probability [0.0 - 1.0] that an offspring will have a randomly generated parent vector");
			System.out.println("T = Number of rounds of offspring production to perform");
			System.out.println("I = How many runs of the simulation to score each offspring with");
		}
	}

}

// 1000 avg 1: 2063.551