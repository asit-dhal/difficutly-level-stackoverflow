--easy questions
update posts_python
set user_label = 'easy'
where stack_id in (22864221, 17271319 , 14110030);

--moderate questions
update posts_python
set user_label = 'moderate'
where stack_id in (21553327, 15376509, 14114570, 14123763);

--difficult questions
update posts_python
set user_label = 'difficult'
where stack_id in (18946662, 17160162 , 14262433);