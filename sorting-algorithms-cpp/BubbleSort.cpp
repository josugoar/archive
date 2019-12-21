#include <SFML/Graphics.hpp>
#include <iostream>

using namespace std;
using namespace sf;

int main() {
	// Render window
	RenderWindow window(VideoMode(509, 250), "BubbleSortVisualized", Style::Close);
	// Window open
	while (window.isOpen()) {
		// Define event
		Event evnt;
		while (window.pollEvent(evnt)) {
			// Window background
			window.clear(Color::Black);
			window.display();
			// Event type
			switch (evnt.type) {
			// Window close
			case Event::Closed:
				window.close();
				break;
			}
		}
		// Define values
		int i; int x; int j; int temp;		    //Iterations
		int column[101];			    //Column array
		srand(time(NULL));			    //Initialize random value to null
		// Draw unsorted values
		for (i = 1; i < 101; i++) {
			int r = rand() % 100;
			column[i] = r;
			// White line
			RectangleShape line1(Vector2f(9, 250));
			line1.setFillColor(Color::White);
			// Black line
			RectangleShape line2(Vector2f(9, 250 - r));
			line2.setFillColor(Color::Black);
			// Set position
			line1.setPosition(5 * (i - 1), 0);
			line2.setPosition(5 * (i - 1), 0);
			// Draw line
			window.draw(line1);
			window.draw(line2);
			window.display();
		}
		// Print unsorted values
		cout << "Unsorted values:\n";
		for (i = 1; i < 101; i++) {
			cout << column[i] << " ";
			if (i % 20 == 0) {
				cout << "\n";
			}
		}
		// Keyboard input required to proceed
		system("pause");
		cout << "\n\n";
		// Bubble sort
		for (i = 1; i < 101; i++) {
			for (j = 1; j < 101 - i; j++) {
				if (column[j] > column[j + 1]) {
					temp = column[j];
					column[j] = column[j + 1];
					column[j + 1] = temp;
				}
			}
			// Draw sorted values
			for (x = 1; x < 101; ++x) {
				// White line
				RectangleShape line1(Vector2f(9, 250));
				line1.setFillColor(Color::White);
				// Black line
				RectangleShape line2(Vector2f(9, 250 - column[x]));
				line2.setFillColor(Color::Black);
				// Set position
				line1.setPosition(5 * (x - 1), 0);
				line2.setPosition(5 * (x - 1), 0);
				// Draw line
				window.draw(line1);
				window.draw(line2);
				window.display();
			}
		}
		// Print sorted values
		cout << "Sorted values:\n";
		for (i = 1; i < 101; i++) {
			cout << column[i] << " ";
			if (i % 20 == 0) {
				cout << "\n";
			}
		}
		// End
		break;
	}
	// Pause
	system("pause");
}
