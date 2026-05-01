import streamlit as st
import requests
st.header("Dashboard    ")

search_form = st.form("search")

options = search_form.multiselect(
    "Select the technologies your are looking for, or enter new options?",
    ["Java", "Python", "devops", "bigdata", "cloud"],
    max_selections=5
)

submit = search_form.form_submit_button("search")
if submit:
    params = [("query", skill) for skill in options]

    response = requests.get(
        "http://localhost:8080/search/skills",
        params=params
    )

    st.write(response.text)