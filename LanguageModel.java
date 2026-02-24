
import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		// Your code goes here
        String window = "";
        char c;

        In in = new In(fileName);
		while ((!in.isEmpty()) && (window.length() < windowLength)) {
			c  = in.readChar();
			window += c;
		}
		while (!in.isEmpty()) {
			c  = in.readChar();
			
			// Checks if the window is already in the map
			List probs = CharDataMap.get(window);
			if (probs == null) {
				probs = new List();
				CharDataMap.put(window, probs);
			}
			// Calculates the counts of the current character.
			probs.update(c);

			
			window += c;
			window = window.substring(1, window.length());
		}

        // The entire file has been processed, and all the characters have been counted.
        // Proceeds to compute and set the p and cp fields of all the CharData objects
        // in each linked list in the map.
		for (List probs : CharDataMap.values())
			calculateProbabilities(probs);
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		int occurrences = 0;
		for (int i = 0; i < probs.getSize(); ++i) {
			occurrences += probs.get(i).count;
		}

		for (int i = 0; i < probs.getSize(); ++i) {
			// calculate probability
			probs.get(i).p = probs.get(i).count / ((double) occurrences); 
            probs.get(i).cp = probs.get(i).p;
            if(i > 0){
                probs.get(i).cp += probs.get(i - 1).cp;
            }
		}
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		// Your code goes here
        double random = this.randomGenerator.nextDouble();
		char ch = ' ';
		for (int i = 0; i < probs.getSize(); ++i) {
			if (random < probs.get(i).cp) {
				ch = probs.get(i).chr;
				break;
			}
		}
		return ch;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		// Your code goes here
        if (initialText.length() < windowLength) {
            return initialText;
        }
        StringBuilder sb = new StringBuilder(initialText);
        for (int i = 0; i < textLength; i++) {
            int start = sb.length() - this.windowLength;
            String currentWindow = sb.substring(start);
            List list = CharDataMap.get(currentWindow);
            if (list == null) {
                break; 
            }
            char nextCh = getRandomChar(list);
            sb.append(nextCh);
        }
        return sb.toString();
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		// Your code goes here
    }
}
