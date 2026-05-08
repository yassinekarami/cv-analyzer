import streamlit as st
import requests
import pandas as pd

st.header("Dashboard")

search_form = st.form("search")

options = search_form.multiselect(
    "Select the technologies your are looking for, or enter new options?",
    ["Java", "Python", "android", "C++", "C", "TensorFlow", "cloud", "javascript"],
    max_selections=5
)

submit = search_form.form_submit_button("search")

if submit:
    params = [("query", skill) for skill in options]

    response = requests.get(
        "http://backoffice:8080/search/skills",
        params={"query": options}
    )

    data = response.json()

    st.write(data)

    df = pd.DataFrame(data)
    df["overallScore"] = df["overallScore"].astype(float)

    df = df.sort_values("overallScore", ascending=False)

    st.subheader("Résultats")
    st.dataframe(df)

    st.subheader("Scores des CV")

    st.bar_chart(df.set_index("filename"))