select 1;
CREATE SCHEMA IF NOT EXISTS dictionary;

INSERT INTO dictionary.FOOD_GUIDE (id, gender, age_group, grains, veggie, dairy, protein) VALUES
(1, 'F', 'ADULT', 7, 6, 2, 2),
(2, 'F', 'FIFTYPLUS', 7, 6, 3, 2),
(3, 'M', 'ADULT', 8, 8, 2, 3),
(4, 'M', 'FIFTYPLUS', 7, 7, 3, 3),
(5, 'O', 'ADULT', 8, 7, 2, 2),
(6, 'O', 'FIFTYPLUS', 7, 6, 3, 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO dictionary.GRAINS (id, name, serving_name, conversion_ratio) VALUES
(1, 'whole grains', 'side', 2),
(2, 'rice', 'side', 2),
(3, 'buckwheat', 'side', 2),
(4, 'oatmeal', 'side', 2),
(5, 'porrige', 'side', 2),
(6, 'pancake', 'piece', 1),
(7, 'crepe', 'piece', 1),
(8, 'bread', 'slice', 1),
(9, 'pastry', 'piece', 1),
(10, 'cookie', 'piece', 0.5),
(11, 'cake/pie', 'slice', 1),
(12, 'pasta', 'dish', 2),
(13, 'noodles', 'dish', 2),
(14, 'dumpling bun', 'piece', 1),
(15, 'bread thins', 'handful', 1),
(16, 'crackers', 'handful', 1),
(17, 'chips', 'handful', 1),
(18, 'french fries', 'serving', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO dictionary.PLANT (id, name, conversion_ratio) VALUES
(1, 'banana', 1),
(1, 'apple', 1),
(3, 'pear', 1),
(4, 'carrot', 2),
(5, 'tomato', 1),
(6, 'cucumber', 1),
(7, 'bell pepper', 1),
(8, 'cabbage', 1),
(9, 'zucchini', 2),
(10, 'eggplant', 2),
(11, 'beets', 1),
(12, 'squash', 1),
(13, 'pumpkin', 1),
(14, 'spinach', 1),
(15, 'orange', 1),
(16, 'watermelon', 1),
(17, 'smoothie', 1),
(18, 'mashed potatoes', 1),
(19, 'mashed squash', 1),
(20, 'potato', 1),
(21, 'sweet potato', 1),
(22, 'salad', 1),
(23, 'side of veggies', 1),
(24, 'mushrooms', 1),
(25, 'nuts and seeds', 1),
(26, 'asparagus', 1),
(27, 'green beans', 1),
(28, 'bok choy', 1),
(29, 'broccoli', 1),
(30, 'brussel sprouts', 1),
(31, 'cauliflower', 1),
(32, 'celery', 1),
(33, 'corn', 1),
(34, 'greens', 1),
(35, 'kale', 1),
(36, 'leeks', 1),
(37, 'lettuce', 1),
(38, 'parsnip', 1),
(39, 'daikon', 1),
(40, 'radishes', 1),
(41, 'turnip', 1),
(42, 'melon', 1),
(43, 'grapes', 1),
(44, 'mango', 1),
(45, 'pineapple', 1),
(46, 'berries', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO dictionary.DAIRY (id, name, serving_name, conversion_ratio) VALUES
(1, 'milk', 'glass', 1),
(2, 'yogurt', 'serving', 1),
(3, 'cheese', 'serving', 1),
(4, 'kefir', 'glass', 2),
(5, 'tvorog', 'serving', 1),
(6, 'milkshake', 'glass', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO dictionary.PROTEIN (id, name, serving_name, conversion_ratio) VALUES
(1, 'chicken', 'serving', 1),
(2, 'meat', 'serving', 1),
(3, 'turkey', 'serving', 1),
(4, 'chicken breast', 'piece', 2),
(5, 'chicken leg', 'piece', 1),
(6, 'tefteli', 'piece', 1),
(7, 'cutlet', 'piece', 1),
(8, 'steak', 'piece', 2),
(9, 'sausage', 'piece', 1),
(10, 'fish', 'serving', 1),
(12, 'shrimps', 'serving', 1),
(12, 'egg', 'item', 0.5),
(13, 'lentils', 'serving', 1),
(14, 'beans', 'serving', 1),
(15, 'legumes', 'serving', 1),
(16, 'tofu', 'serving', 1)
ON CONFLICT (id) DO NOTHING;
