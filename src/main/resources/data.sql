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
