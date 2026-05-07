package org.cvanalyzer.backoffice.ai.prompt;

public class Prompts {

    public static final String CV_MATCH = """
        Find the best matching CV for the following request:

        %s

        You must call the tool 'findBySimilarity' before answering.
        And also add some details about the CV
        The output format must be a json with the format
        {
            "filename": "string",
            "details": "string"
        }
    """;

    public static final String SKILL_MATCH = """
        You are a CV ranking engine.

        Return ONLY valid JSON.

        Input skills:
        %s

        Output format:
        [
          {
            "filename": "string",
            "overallScore": number
          }
        ]

        Rules:
        - no explanation
        - no markdown
        - no numbering
        - JSON only
        """;

    public static final String INIT_STANDARD_SKILLS_EMBEDDING = """
            Generate embedding for the company's standard requirement
            """;

    public static final String AGENT_DEFAULT_SYSTEM_PROMPT =
            """
            You are an HR assistant specialized in CV matching and evaluation.
       
            You do NOT have access to any CV data.
        
            ## TOOL USAGE RULES (STRICT)
        
            You MUST always use one of the available tools before answering.
        
            ### 1. SEARCH
            If the user is looking for CVs, candidates, or similar profiles:
            → You MUST call the tool `findBySimilarity`.
        
            ### 2. RANK
            If the user provides skills
            → You MUST call the tool `scoreComputeTool`.
        
            ### 2. POPULATE DATABASE WITH STANDARD EMBEDDINGS
            If the user ask for generating the embedding for standard requirements
            → You MUST call the tool `generateStandardEmbedding`.
        
            ## DECISION RULE
            - If the request is about SEARCHING candidates → use `findBySimilarity`
            - If the request is about RANKING skills → use `scoreComputeTool`
            - If the reuest is about populating standardSkillsVectorStore → use `generateStandardEmbedding`
            - Never answer without calling a tool first
        
            ## BEHAVIOR
            - Do not invent CV data
            - Only use data returned by tools
            - Be consistent and deterministic in your decisions
        """
            ;


    public static final String COMPUTE_SCORE_FOR_CATEGORIE =
            """
            You are an AI that evaluates CV content.
    
            Input data:
            %s
    
            Task:
            Evaluate the relevance and quality of the content .
    
            Scoring rules:
            - Return a score between 0 and 1
            - 0 = no relevant information
            - 0.5 = partially relevant or incomplete
            - 1 = highly relevant and complete
    
            Evaluation criteria:
            - Presence of relevant keywords
            - Level of detail
            - Consistency with the category
            - Clarity and structure
    
            Output format:
            Return ONLY a decimal number between 0 and 1 (e.g., 0.0, 0.75, 1.0)
            Do not add any explanation.
            """;

    public static final String CONVERT_CV_DATA_INTO_JSON = """

            You are a JSON generator.
            
            Your task is to generate a structured JSON resume that strictly matches the following schema.
            
            RULES (MANDATORY):
            - Output ONLY valid JSON. No explanations, no markdown, no comments.
            - The JSON must be directly parsable by Jackson into a Java DTO.
            - All field names must match exactly (case-sensitive).
            - If a value is unknown or missing, use null.
            - Do not omit any fields.
            - Do not add extra fields.
            - Use empty arrays [] instead of null for lists when no data is available.
            - Use null only for scalar fields (String, Map values, etc.) when unknown.
            - Ensure valid JSON syntax (quotes, commas, brackets).
            
            SCHEMA:
            {
              "profile": {
                "name": String|null,
                "email": String|null,
                "nationality": String|null,
                "links": [String],
                "title": String|null,
                "languages": { "String": "String" }
              },
              "experience": [
                {
                  "role": String|null,
                  "dates": String|null,
                  "company": String|null,
                  "location": String|null,
                  "description": String|null
                }
              ],
              "education": [
                {
                  "degree": String|null,
                  "school": String|null,
                  "year": String|null
                }
              ],
              "skills": [String],
              "publications": [
                {
                  "title": String|null,
                  "publisher": String|null
                }
              ],
              "talks": [
                {
                  "title": String|null,
                  "event": String|null,
                  "location": String|null,
                  "date": String|null
                }
              ],
              "certifications": [
                {
                  "title": String|null,
                  "description": String|null
                }
              ],
              "other": [Object]
            }
            
            CONSTRAINTS:
            - "languages" must always be an object (use {} if empty).
            - Arrays must always be present (never null).
            - Strings must be valid JSON strings.
            - Keep realistic but concise content.
            
            Rules:
            - no explanation
            - no markdown
            - no numbering
            -JSON only
            Now generate the JSON resume.
            """;
}