import streamlit as st
import requests

st.title("Search for profile aaaaaaaaa")

# Init history
if "messages" not in st.session_state:
    st.session_state.messages = []

# Display history
for message in st.session_state.messages:
    with st.chat_message(message["role"]):
        st.markdown(message["content"])

# Input user
if prompt := st.chat_input("What is up?"):
    # Save user message
    st.session_state.messages.append({"role": "user", "content": prompt})

    with st.chat_message("user"):
        st.markdown(prompt)

    # Call backend
    try:
        response = requests.get("http://localhost:8080/search", params={"query": prompt})

        if response.status_code == 200:
            data = response.text
        else:
            data = f"Erreur backend: {response.status_code}"

    except Exception as e:
        data = f"Erreur connexion: {e}"

    # Display assistant response
    with st.chat_message("assistant"):
        st.markdown(data)

    # Save assistant response
    st.session_state.messages.append({"role": "assistant", "content": data})