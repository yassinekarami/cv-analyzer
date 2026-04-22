import streamlit as st

st.header("Dashboard ")

options = st.multiselect(
    "Select the technologies your are looking for, or enter new options?",
    ["Java", "devops", "bigdata", "cloud"],
    max_selections=5,
    accept_new_options=True,
)

st.write("You selected:", options)