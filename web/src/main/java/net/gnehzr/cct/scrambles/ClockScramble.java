package net.gnehzr.cct.scrambles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.puzzletimer.solvers.RubiksClockSolver;
import com.puzzletimer.solvers.RubiksClockSolver.State;

import net.gnehzr.cct.scrambles.Scramble;
import net.gnehzr.cct.scrambles.Scramble.JavascriptArray;

@SuppressWarnings("unused") 
public class ClockScramble extends Scramble {
	private static final String[][] FACE_NAMES_COLORS = {{},{}};
	private static final String PUZZLE_NAME = "Clock";
	private static final String[] VARIATIONS = { "Clock" };
	private static final int[] DEFAULT_LENGTHS = {10};
	private static final int DEFAULT_UNIT_SIZE = 230;
	private static final String[] ATTRIBUTES = new String[] { "%%verbose%%" };
//	private static final Pattern TOKEN_REGEX = Pattern.compile("^(\\S)(.*)$");
	private boolean verbose;
	public static JavascriptArray<Integer> posit;
	public static int[] pins = new int[]{0,0,0,0};
	public static JavascriptArray<JavascriptArray<Integer>> moves = new JavascriptArray<Scramble.JavascriptArray<Integer>>();
	
	public String[][] getFaceNamesColors() {
		return FACE_NAMES_COLORS;
	}
	
	public ClockScramble(String variation, String s, String generatorGroup, String... attrs) throws InvalidScrambleException {
		super(s);
		if(!setAttributes(attrs)) throw new InvalidScrambleException(s);
	}

	public ClockScramble(String variation, int length, String generatorGroup, String... attrs) {
		this.length = length;
		moves.add(new JavascriptArray<Integer>(1,1,1,1,1,1,0,0,0,  -1,0,-1,0,0,0,0,0,0));
		moves.add(new JavascriptArray<Integer>(0,1,1,0,1,1,0,1,1,  -1,0,0,0,0,0,-1,0,0));
		moves.add(new JavascriptArray<Integer>(0,0,0,1,1,1,1,1,1,  0,0,0,0,0,0,-1,0,-1));
		moves.add(new JavascriptArray<Integer>(1,1,0,1,1,0,1,1,0,  0,0,-1,0,0,0,0,0,-1));
		moves.add(new JavascriptArray<Integer>(0,0,0,0,0,0,1,0,1,  0,0,0,-1,-1,-1,-1,-1,-1));
		moves.add(new JavascriptArray<Integer>(1,0,0,0,0,0,1,0,0,  0,-1,-1,0,-1,-1,0,-1,-1));
		moves.add(new JavascriptArray<Integer>(1,0,1,0,0,0,0,0,0,  -1,-1,-1,-1,-1,-1,0,0,0));
		moves.add(new JavascriptArray<Integer>(0,0,1,0,0,0,0,0,1,  -1,-1,0,-1,-1,0,-1,-1,0));
		moves.add(new JavascriptArray<Integer>(0,1,1,1,1,1,1,1,1,  -1,0,0,0,0,0,-1,0,-1));
		moves.add(new JavascriptArray<Integer>(1,1,0,1,1,1,1,1,1,  0,0,-1,0,0,0,-1,0,-1));
		moves.add(new JavascriptArray<Integer>(1,1,1,1,1,1,1,1,0,  -1,0,-1,0,0,0,0,0,-1));
		moves.add(new JavascriptArray<Integer>(1,1,1,1,1,1,0,1,1,  -1,0,-1,0,0,0,-1,0,0));
		moves.add(new JavascriptArray<Integer>(1,1,1,1,1,1,1,1,1,  -1,0,-1,0,0,0,-1,0,-1));
		moves.add(new JavascriptArray<Integer>(1,0,1,0,0,0,1,0,1,  -1,-1,-1,-1,-1,-1,-1,-1,-1));
		setAttributes(attrs);
	}

	private boolean setAttributes(String... attributes){
		verbose=false;
		for(String attr : attributes)
			if(attr.equals(ATTRIBUTES[0])){
				verbose = true;
			}
		if(scramble != null) {
			return validateScramble();
		}
		generateScramble();
		return true;
	}


	private boolean validateScramble() {
		posit = new JavascriptArray<Integer>(0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0);
		State state = stateFromSequence((getSequence() != null) ? getSequence() : scramble.split(" "));
		for (int j=0; j<18; j++){
			posit.set(j, state.clocks[j]);
		}
		for (int i=0; i<state.pinsDown.length; i++) {
			pins[i] = (state.pinsDown[i])? 1 : 0;
		}
		return true;
	}
	
	private void generateScramble(){
		scramble = "";
		posit = new JavascriptArray<Integer>(0,0,0,0,0,0,0,0,0,  0,0,0,0,0,0,0,0,0);
		JavascriptArray<Integer> seq = new JavascriptArray<Integer>();
		StringBuilder scram = new StringBuilder();
		String[] peg={"U","d"};
		String[] pegs={"UUdd ","dUdU ","ddUU ","UdUd "};
		String[] upegs={"dUUU ","UdUU ","UUUd ","UUdU ","UUUU "};
		for (int i=0; i<14; i++){
			seq.set(i, (int)Math.floor(Math.random()*12)-5);
		}
		for (int i=0; i<14; i++){
			for (int j=0; j<18; j++){
				posit.set(j, posit.get(j)+seq.get(i)*moves.get(i).get(j));
			}
		}
		for (int j=0; j<18; j++){
			posit.set(j, posit.get(j)%12);
			while (posit.get(j)<=0) posit.set(j, posit.get(j)+12);
		}
		for(int x=0; x<4; x++){
			if (verbose){
				scram.append(pegs[x]);
			}
			scram.append("u=").append(seq.get(x)).append(",d=").append(seq.get(x+4)).append(" / ");
		}
		for(int x=0;x<5; x++){
			if (verbose){	
				scram.append(upegs[x]);
			}
			scram.append("u=").append(seq.get(x+8)).append(" / ");
		}
		if (verbose){
			scram.append("dddd ");
		}
		scram.append("d=").append(seq.get(13)).append(" / ");
		for(int x=0;x<4;x++){
			pins[x] = random(2);
			scram.append(peg[pins[x]]);
		}
		
		if(scram.length() > 0)
			scramble = scram.substring(0);
	}

	public BufferedImage getScrambleImage(int gap, int clockSize, Color[] colorScheme) {
		validateScramble();
		Dimension dim = getImageSize(gap, clockSize, null);
		BufferedImage buffer = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
		drawClock(buffer.createGraphics(), gap, clockSize, colorScheme);
		return buffer;
	}
	
	public static Dimension getImageSize(int gap, int clockSize, String variation) {
		return new Dimension((clockSize + 3*gap) * 2, clockSize + 4*gap);
	}
	
	public static int getNewUnitSize(int width, int height, int gap, String variation) {
		return (int) Math.round(Math.min((width - 6*gap) / 2, (height - 4*gap)));
	}
	
	private void drawClock(Graphics2D g, int gap, int clockSize, Color[] colorScheme) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// front
		Ellipse2D.Double clockFront = new Ellipse2D.Double(gap*2, gap*2, clockSize, clockSize);
		g.setColor(Color.CYAN);
		g.fill(clockFront);
		g.setColor(Color.BLACK);
		g.draw(clockFront);
		
		// front dials
		int x1 = calcRelativeDimension(clockSize, 32)+gap*2;
		int x2 = x1+calcRelativeDimension(clockSize, 42+20);
		int x3 = x2+calcRelativeDimension(clockSize, 42+20);
		int y1 = calcRelativeDimension(clockSize, 34)+gap*2;
		int y2 = y1+calcRelativeDimension(clockSize, 41+20);
		int y3 = y2+calcRelativeDimension(clockSize, 41+20);

		drawDial(g, x1, y1, clockSize, posit.get(0), true);
		drawDial(g, x2, y1, clockSize, posit.get(1), true);
		drawDial(g, x3, y1, clockSize, posit.get(2), true);
		
		drawDial(g, x1, y2, clockSize, posit.get(3), true);
		drawDial(g, x2, y2, clockSize, posit.get(4), true);
		drawDial(g, x3, y2, clockSize, posit.get(5), true);
		
		drawDial(g, x1, y3, clockSize, posit.get(6), true);
		drawDial(g, x2, y3, clockSize, posit.get(7), true);
		drawDial(g, x3, y3, clockSize, posit.get(8), true);
		
		// front pins
		int x = calcRelativeDimension(clockSize, 80)+gap*2;
		int y = calcRelativeDimension(clockSize, 80)+gap*2;
		drawPin(g, clockSize, x, y, pins[0]==0);
		drawPin(g, clockSize, x+calcRelativeDimension(clockSize, 61), y, pins[1]==0);
		drawPin(g, clockSize, x+calcRelativeDimension(clockSize, 61), y+calcRelativeDimension(clockSize, 61), pins[3]==0);
		drawPin(g, clockSize, x, y+calcRelativeDimension(clockSize, 61), pins[2]==0);
		
		// back
		Ellipse2D.Double clockBack = new Ellipse2D.Double(gap*4 + clockSize, gap*2, clockSize, clockSize);
		g.setColor(Color.BLUE);
		g.fill(clockBack);
		g.setColor(Color.BLACK);
		g.draw(clockBack);
		
		// back dials
		x1 = gap*4 + clockSize + calcRelativeDimension(clockSize, 32);
		x2 = x1+calcRelativeDimension(clockSize, 42+20);
		x3 = x2+calcRelativeDimension(clockSize, 42+20);
		drawDial(g, x1, y1, clockSize, posit.get(9), false);
		drawDial(g, x2, y1, clockSize, posit.get(10), false);
		drawDial(g, x3, y1, clockSize, posit.get(11), false);
		
		drawDial(g, x1, y2, clockSize, posit.get(12), false);
		drawDial(g, x2, y2, clockSize, posit.get(13), false);
		drawDial(g, x3, y2, clockSize, posit.get(14), false);
		
		drawDial(g, x1, y3, clockSize, posit.get(15), false);
		drawDial(g, x2, y3, clockSize, posit.get(16), false);
		drawDial(g, x3, y3, clockSize, posit.get(17), false);
		
		// back pins
		x += clockSize+gap*2;
		drawPin(g, clockSize, x, y, pins[0]!=0);
		drawPin(g, clockSize, x+calcRelativeDimension(clockSize, 61), y, pins[1]!=0);
		drawPin(g, clockSize, x+calcRelativeDimension(clockSize, 61), y+calcRelativeDimension(clockSize, 61), pins[3]!=0);
		drawPin(g, clockSize, x, y+calcRelativeDimension(clockSize, 61), pins[2]!=0);
	}
	
	private void drawPin(Graphics2D g, int clockSize, int x, int y, boolean pinUp) {
		int pinSize = calcRelativeDimension(clockSize, 10);
		Ellipse2D.Double pin = new Ellipse2D.Double(x, y, pinSize, pinSize);
		if (pinUp) {
			g.setColor(Color.YELLOW);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fill(pin);
		g.setColor(Color.BLACK);
		g.draw(pin);
	}
	
	private void drawDial(Graphics2D g, int x, int y, int clockSize, int value, boolean isFront) {
		int dialSize = calcRelativeDimension(clockSize, 42);
		Ellipse2D.Double dial = new Ellipse2D.Double(x, y, dialSize, dialSize);
		if (isFront) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.CYAN);
		}
		g.fill(dial);
		g.setColor(Color.BLACK);
		g.draw(dial);
		int r = calcRelativeDimension(clockSize, 25);
		int pos = calcRelativeDimension(clockSize, 20);
		int pegSize = calcRelativeDimension(clockSize, 2);
		int tr = calcRelativeDimension(clockSize, 21);
		for (int i=1; i<=12; i++) {
			int degree = 360 / 12 * i;
			double radian = degree * Math.PI/180 - Math.PI/2;
			
			Ellipse2D.Double peg = new Ellipse2D.Double(r*Math.cos(radian)+x+pos, r*Math.sin(radian)+y+pos, pegSize, pegSize);
			g.draw(peg);
		}
		g.draw(setTime(x+tr, y+tr, value, clockSize));
	}

	private int calcRelativeDimension(int clockSize, int value) {
		return (int) Math.round(clockSize * value / DEFAULT_UNIT_SIZE);
	}
	
	private GeneralPath setTime(int x, int y, int value, int clockSize) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x, y);
		int degree = 360 / 12 * value;
		double radian = degree * Math.PI/180 - Math.PI/2;
		int r = calcRelativeDimension(clockSize, 21);
		p.lineTo(r*Math.cos(radian)+x, r*Math.sin(radian)+y);
		p.closePath();
		return p;
	}
	
	private static class State {
        public int[] clocks;
        public boolean[] pinsDown;

        public State(int[] clocks, boolean[] pinsDown) {
            this.clocks = clocks;
            this.pinsDown = pinsDown;
        }

        public State rotateWheel(boolean[] pinsDown, int wheel, int turns) {
            int[] newClocks = new int[18];
            for (int i = 0; i < newClocks.length; i++) {
                newClocks[i] = this.clocks[i];
            }

            // front
            boolean[] affectedClocks = new boolean[18];
            for (int i = 0; i < affectedClocks.length; i++) {
                affectedClocks[i] = false;
            }

            if (pinsDown[wheel]) {
                for (int i = 0; i < 4; i++) {
                    if (pinsDown[i]) {
                        affectedClocks[wheelsClockFront[i]] = true;
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (!pinsDown[i]) {
                        for (int clock : pinsClocksFront[i]) {
                            affectedClocks[clock] = true;
                        }
                    }
                }
            }

            for (int i = 0; i < this.clocks.length; i++) {
                if (affectedClocks[i]) {
                    newClocks[i] = (newClocks[i] + 12 + turns) % 12;
                }
            }

            // back
            for (int i = 0; i < affectedClocks.length; i++) {
                affectedClocks[i] = false;
            }

            if (!pinsDown[wheel]) {
                for (int i = 0; i < 4; i++) {
                    if (!pinsDown[i]) {
                        affectedClocks[wheelsClockBack[i]] = true;
                    }
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (pinsDown[i]) {
                        for (int clock : pinsClocksBack[i]) {
                            affectedClocks[clock] = true;
                        }
                    }
                }
            }

            for (int i = 0; i < this.clocks.length; i++) {
                if (affectedClocks[i]) {
                    newClocks[i] = (newClocks[i] + 12 - turns) % 12;
                }
            }

            return new State(newClocks, pinsDown);
        }

        public static int[] wheelsClockFront;
        public static int[] wheelsClockBack;
        public static int[][] pinsClocksFront;
        public static int[][] pinsClocksBack;
        public static State id;

        static {
            wheelsClockFront = new int[] {
                0, 2, 6, 8
            };

            wheelsClockBack = new int[] {
                11, 9, 17, 15
            };

            pinsClocksFront = new int[][] {
                { 0, 1, 3, 4 },
                { 1, 2, 4, 5 },
                { 3, 4, 6, 7 },
                { 4, 5, 7, 8 },
            };

            pinsClocksBack = new int[][] {
                { 10, 11, 13, 14 },
                {  9, 10, 12, 13 },
                { 13, 14, 16, 17 },
                { 12, 13, 15, 16 },
            };

            id = new State(
                new int[] {
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 0,

                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 0,
                },
                new boolean[] {
                    false, false,
                    false, false,
                });
        }
    }
	
	private State stateFromSequence(String[] sequence) {
        Pattern pattern2 = Pattern.compile("([Ud]{4}) ([ud])=(-?\\d),([ud])=(-?\\d)");
        Pattern pattern1 = Pattern.compile("([Ud]{4}) ([ud])=(-?\\d)");
        Pattern pattern0 = Pattern.compile("([Ud]{4})");

        State state = State.id;
        for (String move : sequence) {
            // two wheels
            Matcher matcher2 = pattern2.matcher(move.toString());
            if (matcher2.find()) {
                String pins = matcher2.group(1);
                boolean[] pinsDown = new boolean[4];
                for (int i = 0; i < 4; i++) {
                    pinsDown[i] = pins.charAt(i) != 'U';
                }

                int wheel1 = -1;
                for (int i = 0; i < 4; i++) {
                    if (matcher2.group(2).charAt(0) == (pinsDown[i] ? 'd' : 'u')) {
                        wheel1 = i;
                    }
                }

                int turns1 = Integer.parseInt(matcher2.group(3));
                state = state.rotateWheel(pinsDown, wheel1, turns1);

                int wheel2 = -1;
                for (int i = 0; i < 4; i++) {
                    if (matcher2.group(4).charAt(0) == (pinsDown[i] ? 'd' : 'u')) {
                        wheel2 = i;
                    }
                }

                int turns2 = Integer.parseInt(matcher2.group(5));
                state = state.rotateWheel(pinsDown, wheel2, turns2);

                continue;
            }

            // one wheel
            Matcher matcher1 = pattern1.matcher(move.toString());
            if (matcher1.find()) {
                String pins = matcher1.group(1);
                boolean[] pinsDown = new boolean[4];
                for (int i = 0; i < 4; i++) {
                    pinsDown[i] = pins.charAt(i) != 'U';
                }

                int wheel = -1;
                for (int i = 0; i < 4; i++) {
                    if (matcher1.group(2).charAt(0) == (pinsDown[i] ? 'd' : 'u')) {
                        wheel = i;
                    }
                }

                int turns = Integer.parseInt(matcher1.group(3));
                state = state.rotateWheel(pinsDown, wheel, turns);

                continue;
            }

            // no rotation
            Matcher matcher0 = pattern0.matcher(move.toString());
            if (matcher0.find()) {
                String pins = matcher0.group(1);
                boolean[] pinsDown = new boolean[4];
                for (int i = 0; i < 4; i++) {
                    pinsDown[i] = pins.charAt(i) != 'U';
                }

                state = state.rotateWheel(pinsDown, 0, 0);
            }
        }

        return state;
    }
}

