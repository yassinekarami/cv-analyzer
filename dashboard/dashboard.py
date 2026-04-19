import streamlit as st

create_page = st.Page("page/upload_cv_page.py", title="upload cv", icon=":material/upload:")
delete_page = st.Page("page/search_cv_page.py", title="search cv", icon=":material/search:")

pg = st.navigation([create_page, delete_page])
st.set_page_config(page_title="Data manager", page_icon=":material/edit:")
pg.run()