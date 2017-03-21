# Pokemon-Rock-Paper-Scissors
Rock paper scissors vs. a computer. Fire beats grass, grass beats water, and water beats fire!

Computer learns user guess patterns by storing those patterns into a hashmap.

By testing multiple pattern lengths, a pattern length of 4 seems to be a good medium between a 
guess pattern of length 1-2 (too short) or greater than 4 (too long). 3 is also an acceptable length.

Computer stores patterns by updating and adding the current user pattern. For example, the first
user pattern is F, W, G, G. The next turn, the user plays W. Now, a pattern of W, G, G, W is stored
into the hashmap - notice the first item in the pattern was deleted and the newest item is appended.
The computer checks if this pattern is already in the hashmap, and if so, it increments the value
associated with that key.

To make a prediction, the computer takes the past 3 guesses, and appends each possible guess temporarily
and finds the pattern with the highest frequency. For example,

Past three guesses: F, F, G
What is the user most likely to play next?

Key: F, F, G, F     Value: 3
Key: F, F, G, G     Value: 6
Key: F, F, G, W     Value: 2

Therefore, in this case the computer guesses grass, G,  because it holds the highest frequency in the hashmap.

Player can choose to play a computer that already has playing history, stored using an ObjectOutputStream
