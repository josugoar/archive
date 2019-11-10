#include <iostream>
#include <math.h>
#include <SFML/Graphics.hpp>
#define PI 3.14159265359
using namespace std;
using namespace sf;

int main() {

	int i = 0,		//Six iteration
		w = 0,		//Fifth iteration
		t = 0,		//Fourth iteration
		l = 0,		//Third iteration
		k = 0,		//Second iteration
		j = 0;		//First iteration

	float a_1 = 0,		//First iteration angle
		a_2 = 0,		//Second iteration angle
		a_3 = 0,		//Third iteration angle
		a_4 = 0,		//Fourth iteration angle
		a_5 = 0,		//Fifth iteration angle
		a_6 = 0,		//Six iteration angle

		z_1 = 0,		//First iteration longitude
		z_2 = 0,		//Second iteration longitude
		z_3 = 0,		//Third iteration longitude
		z_4 = 0,		//Fourth iteration longitude
		z_5 = 0,		//Fifth iteration longitude
		z_6 = 0,		//Six iteration longitude

		x = 0,			//Horizontal axis position
		x_0 = 0,		//Backup horizontal axis position
		y = 0,			//Vertical axis position
		y_0 = 0,		//Backup vertical axis position
		max_x = 1250,	//Maximum window horizontal longitude
		max_y = 500;	//Maximum window vertical longitude

	//Calculate starting point
	y = 4 * max_y / 5;
	y_0 = y;

	//Print magnitudes
	cout << "	ANGLE		LONGITUDE	POSITION_X	POSITION_Y" << endl;

	//Render window
	RenderWindow window(VideoMode(max_x, max_y), "Fractal", Style::Close);

	//First iteration
	while (j < 4) {
		//Calcuate longitude
		z_1 = max_x / 3;
		//Calculate angle
		switch (j) {
		case 0:
			a_1 = 0;
			break;
		case 1:
			a_1 = +60;
			break;
		case 2:
			a_1 = -60;
			break;
		case 3:
			a_1 = 0;
			break;
		};

		//Second iteration
		while (k < 4) {
			//Calcuate longitude
			z_2 = z_1 / 3;
			//Calculate angle
			switch (k) {
			case 0:
				a_2 = a_1;
				break;
			case 1:
				a_2 = a_1 + 60;
				break;
			case 2:
				a_2 = a_1 - 60;
				break;
			case 3:
				a_2 = a_1;
				break;
			};

			//Third iteration
			while (l < 4) {
				//Calcuate longitude
				z_3 = z_2 / 3;
				//Calculate angle
				switch (l) {
				case 0:
					a_3 = a_2;
					break;
				case 1:
					a_3 = a_2 + 60;
					break;
				case 2:
					a_3 = a_2 - 60;
					break;
				case 3:
					a_3 = a_2;
					break;
				};

				//Fourth iteration
				while (t < 4) {
					//Calculate longitude
					z_4 = z_3 / 3;
					//Calculate angle
					switch (t) {
					case 0:
						a_4 = a_3;
						break;
					case 1:
						a_4 = a_3 + 60;
						break;
					case 2:
						a_4 = a_3 - 60;
						break;
					case 3:
						a_4 = a_3;
						break;
					};

					//Fifth iteration
					while (w < 4) {
						//Calculate longitude
						z_5 = z_4 / 3;
						//Calculate angle
						switch (w) {
						case 0:
							a_5 = a_4;
							break;
						case 1:
							a_5 = a_4 + 60;
							break;
						case 2:
							a_5 = a_4 - 60;
							break;
						case 3:
							a_5 = a_4;
							break;
						};

						//Sixth iteration
						while (i < 4) {
							//Calculate longitude
							z_6 = z_5 / 3;
							//Calculte angle
							switch (i) {
							case 0:
								a_6 = a_5;
								break;
							case 1:
								a_6 = a_5 + 60;
								break;
							case 2:
								a_6 = a_5 - 60;
								break;
							case 3:
								a_6 = a_5;
								break;
							};

							//Print values
							cout << "	" << a_6 << "		" << z_6 << "		" << x << "		" << y << endl;
							//Draw lines
							RectangleShape line(Vector2f(z_6, 1));
							line.setFillColor(sf::Color(250, 250, 250));
							line.setPosition(x, y);
							line.setRotation(-a_6);
							window.draw(line);

							//Calculate position
							x = cos(a_6 * PI / 180) * z_6 + x_0;
							x_0 = x;
							y = y_0 - sin(a_6 * PI / 180) * z_6;
							y_0 = y;

							//Move to next iteration
							i++;
						}

						//Move to next iteration
						w++;
						i = 0;
					}

					//Move to next iteration
					t++;
					w = 0;
				}

				//Move to next iteration
				l++;
				t = 0;
			}

			//Move to next iteration
			k++;
			l = 0;
		}

		//Move to next iteration
		j++;
		k = 0;
	}

	//Display lines
	window.display();

	//Close window
	while (window.isOpen()) {
		Event event;
		while (window.pollEvent(event)) {
			if (event.type == Event::Closed) {
				window.close();
			}
		}
	}
}