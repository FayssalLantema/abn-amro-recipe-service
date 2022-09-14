INSERT INTO person (username, password)
VALUES ('Quintor', 'Password');
INSERT INTO person (username, password)
VALUES ('User2', 'Password');

INSERT INTO ingredient (name, vegetarian)
VALUES ('Spinach', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Potato', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Tomato', true);

INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Lasagne with spinach', 3, 1);
INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Potato with tomato', 3, 2);
INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Lasagne with spinach', 4, 2);

INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('300G in the Oven at 200 degrees for 10 minutes', 1, 1);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('200G in the Oven', 2, 2);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('300G in the Oven at 200 degrees for 10 minutes', 3, 2);



