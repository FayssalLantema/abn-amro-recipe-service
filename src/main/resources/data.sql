INSERT INTO ingredient (name, vegetarian)
VALUES ('Carrot', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Corn', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Pumpkin', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Potato', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Onion', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Sweet Potato', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Radish', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Celery', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Eggplant', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Cauliflower', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Broccoli', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Artichoke', true);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Brussels Sprout', true);

INSERT INTO ingredient (name, vegetarian)
VALUES ('Meat', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Chicken', false);
INSERT INTO ingredient (name, vegetarian)
VALUES ('Rib Eye', false);

INSERT INTO person (username, password)
VALUES ('Fayssal', 'Password');

INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Lasagne with Carrot', 3, 1);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('300G in the Oven at 200 degrees for 10 minutes', 1, 1);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('Boil hot water and put Broccoli in it for 15 minutes', 11, 1);

INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Potato with Pumpkin', 2, 1);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('300G in the Oven at 200 degrees for 10 minutes', 2, 2);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('Boil hot water and put Broccoli in it for 15 minutes', 11, 2);

INSERT INTO person (username, password)
VALUES ('Mark', 'Password');
INSERT INTO recipe (name, number_of_servings, created_by_id)
VALUES ('Potato with Pumpkin', 2, 2);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('300G in the Microwave at 200 degrees for 10 minutes', 2, 3);
INSERT INTO instruction (way_of_preperation, ingredient_id, recipe_id)
VALUES ('Boil hot water and put Broccoli in it for 15 minutes', 11, 3);








