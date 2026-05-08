📄 CV Analyzer

A small AI-powered project that analyzes CVs and extracts structured insights using RAG (Retrieval-Augmented Generation), Spring AI, and LLM tooling.

This project is part of my learning journey into building real-world AI applications beyond simple prompt-based systems.

🚀 What it does

The system takes a CV (PDF) as input and generates structured information such as:

Skills
Work experience
Education
Summary

Instead of relying on a single LLM call, the system uses a RAG pipeline to ground responses in the actual CV content.

🧠 Key concepts used
🔍 RAG (Retrieval-Augmented Generation)

The CV is split into chunks, embedded, and stored in a vector database. Relevant sections are retrieved at query time to improve accuracy.

☕ Spring AI

Used to orchestrate LLM interactions, manage prompts, and calling tools

🧩 Tool Calling

Tools are used to:

Parse and extract CV text
Retrieve relevant context
Format structured outputs
✍️ Prompt Engineering

Prompts are designed to:

Enforce structured JSON output
Ensure consistent formatting

🏗️ Architecture
<img width="885" height="2554" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/a0ea1647-df81-41d1-b9cc-d2ef869d46c4" />

⚙️ Workflow
Upload CV
User uploads a PDF file
Parse document
Extract raw text from PDF
Chunking
Split text into meaningful sections
Embeddings
Convert chunks into vector representations
Storage
Store embeddings in a vector database
Retrieval (RAG)
Retrieve relevant CV sections based on query
LLM processing
Spring AI sends context + prompt + tools to LLM
Output
Returns structured JSON response

🧪 Example output
<img width="720" height="305" alt="Capture d’écran du 2026-05-08 17-28-18" src="https://github.com/user-attachments/assets/832f5c5a-3d05-4829-bef8-d028d28ffa20" />


🛠️ Tech Stack
Java 21
Spring Boot
Spring AI
Vector Database (PGVector / similar)
LLM API (OpenAI or compatible provider)
Apache PDFBox (or equivalent PDF parser)
Maven

