#include <iostream>
#include <SFML/Graphics.hpp>

using namespace std;
using namespace sf;

int main() {
	// Define prime number values
	int i = 0,					//Prime number iterations
		p_numb = 1,				//Prime number
		a = 1,
		b = 0;
	int p_array[8769];				//Prime number array
	//P rime number calculation
	for (i = 0; i < 8769; p_numb++) {
		for (a = 1; a <= p_numb; a++) {
			if (p_numb % a == 0) {
				b = b + a;
			}
		}
		if (b == p_numb + 1) {
			p_array[i] = p_numb;
			i++;
		}
		a = 1;
		b = 0;
	}
	// Define ulam spiral values
	int x = 150,					// Ulam spiral horizontal axis
		y = 150;				// Ulam spiral vertical axis
		i = 1;					// Ulam siral iterations
	int ulam_array[301][301] = { 0 };		// Ulam spiral number array
		ulam_array[150][150] = 1;		// Initial ulam spiral value
	// Ulam spiral iteratons
	while (i < 90601) {
		// Check number in the right
		while (i < 90601) {
			x++;
			i++;
			if (ulam_array[y][x] == 0) {
				ulam_array[y][x] = i;
				break;
			}
			else {
				x--;
				y++;
				ulam_array[y][x] = i;
			}
		}
		// Check number above
		while (i < 90601) {
			y--;
			i++;
			if (ulam_array[y][x] == 0) {
				ulam_array[y][x] = i;
				break;
			}
			else {
				y++;
				x++;
				ulam_array[y][x] = i;
			}
		}
		// Check number in the left
		while (i < 90601) {
			x--;
			i++;
			if (ulam_array[y][x] == 0) {
				ulam_array[y][x] = i;
				break;
			}
			else {
				x++;
				y--;
				ulam_array[y][x] = i;
			}
		}
		// Check number below
		while (i < 90601) {
			y++;
			i++;
			if (ulam_array[y][x] == 0) {
				ulam_array[y][x] = i;
				break;
			}
			else {
				y--;
				x--;
				ulam_array[y][x] = i;
			}
		}
	}
	// Define values to compare between  arrays
	x = 0;						// Ulam spiral horizontal axis
	y = 0;						// Ulam spiral vertical axis
	i = 0;						// Comparison iterations
	a = 0;
	b = 0;
	// Compare prime values between arrays
	while (i < 90601) {
		for (a = 0; a <= 8769; a++) {
			if (ulam_array[y][x] == p_array[a]) {
				b = p_array[a];
			}
		}
		ulam_array[y][x] = 0;
		ulam_array[y][x] = b;
		b = 0;
		x++;
		if (x > 301) {
			x = 0;
			y++;
		}
		if (y > 301) {
			break;
		}
		i++;
	}
	// Render SFML window
	sf::RenderWindow window(sf::VideoMode(301, 301), "Ulam Spiral", sf::Style::Close);
	// Render prime number pixels
	x = 0;
	y = 0;
	while (x <= 301) {
		if (ulam_array[y][x] != 0) {
			RectangleShape pixel;
			pixel.setSize(Vector2f(1, 1));
			pixel.setPosition(y, x);
			pixel.setFillColor(Color::White);
			window.draw(pixel);
		}
		x++;
		if (x > 301) {
			x = 0;
			y++;
		}
		if (y > 301) {
			break;
		}
	}
	// Draw initial pixel
	RectangleShape pixel;
	pixel.setSize(Vector2f(1, 1));
	pixel.setPosition(150, 150);
	pixel.setFillColor(Color::Red);
	window.draw(pixel);
	// Close window
	while (window.isOpen()) {
		Event event;
		while (window.pollEvent(event)) {
			if (event.type == Event::Closed) {
				window.close();
			}
		}
		window.display();
		system("pause");
	}
	return 0;
}
