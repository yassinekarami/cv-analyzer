import streamlit as st

upload_cv_page = st.Page("page/upload_cv_page.py", title="upload cv", icon=":material/upload:")
search_cv_page = st.Page("page/search_cv_page.py", title="search cv", icon=":material/search:")
overview_dashboard_page = st.Page("page/overview_dashboard.py", title="dashboard", icon=":material/insert_chart:")


pg = st.navigation([upload_cv_page, search_cv_page, overview_dashboard_page])
st.set_page_config(page_title="Data manager", page_icon=":material/edit:")
pg.run()