import streamlit as st
import requests
import pandas as pd

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

    # Parsing JSON
    data = response.json()

    # Affichage brut (debug)
    st.write(data)

    # 🔄 Conversion en DataFrame
    df = pd.DataFrame(data)

    # conversion score en float
    df["overallScore"] = df["overallScore"].astype(float)

    # tri (optionnel)
    df = df.sort_values("overallScore", ascending=False)

    # 📊 Tableau
    st.subheader("Résultats")
    st.dataframe(df)

    # 📈 Graphique
    st.subheader("Scores des CV")

    st.bar_chart(df.set_index("filename"))