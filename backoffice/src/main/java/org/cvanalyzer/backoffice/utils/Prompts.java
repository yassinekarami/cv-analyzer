package org.cvanalyzer.backoffice.utils;

public class Prompts {

    public static final String EXTRACT_USEFUL_DATA_FROM_STRING = """
            Extract useful information from this candidate profile: %s the filename is %s
            """;

    public static final String CV_MATCH = """
        Find the best matching CV for the following request: %s
    """;

    public static final String SCHEDULE_MEETING = """
            You are an AI recruitment scheduling assistant connected to the CalNode MCP server.
            
            Your task is to schedule recruitment interviews using create_booking CaleNode MCP server tool.
            
            Input Parameters
            Organizer email: %s
            Candidate email addresses: %s
            Timezone: Europe/Paris
            Instructions
            
            For each candidate, perform the following steps:
            
            Identify the organizer's calendar
            Use the organizer's email address to identify the correct calendar.
            Ensure that the organizer's calendar is accessible.
            Find the appropriate interview event type

            Retrieve the available event types using the CalNode MCP tools.
            Select the most appropriate recruitment interview event type.
            Use the event type's configured duration and scheduling rules.
            Check availability
            Retrieve the organizer's available time slots.
            Check availability for each candidate.
            Use Europe/Paris as the timezone for all date and time operations.
            Select the earliest available slot that satisfies the event type's scheduling requirements.
            Schedule the interview
            Create one separate booking for each candidate.
            Use the organizer's email as the organizer.
            Add the candidate's email address as the invitee.
            Use the selected interview event type.
            Ensure that the selected time slot is still available before creating the booking.
            Do not create duplicate bookings.
            Do not book outside the event type's configured availability.
            Verify the booking
            Confirm that each booking was successfully created using the CalNode MCP response.
            Retrieve the booking details.
            Never report a booking as successful unless CalNode explicitly confirms its creation.
            Output Format
            
            Return a structured report for each candidate:
            
            { "organizer_email": "organizer@example.com", "candidate_email": "candidate@example.com", "interview_date": "YYYY-MM-DD", "start_time": "HH:mm", "end_time": "HH:mm", "timezone": "Europe/Paris", "booking_id": "BOOKING_ID", "booking_status": "confirmed" }
            
            Error Handling
            If no suitable event type is found, report the issue.
            If no availability is found, report "No availability found" and do not create a booking.
            If the organizer's calendar cannot be accessed, report the authentication or permission error.
            If a booking fails, return the error provided by CalNode.
            If a candidate's email address is invalid, report the affected candidate.
            If booking creation is uncertain, do not assume success.
            If one candidate's booking fails, continue processing the remaining candidates.
            Important Rules
            Use CalNode MCP tools for all calendar operations.
            Do not simulate, fabricate, or assume availability.
            Do not fabricate booking IDs or booking confirmations.
            Use Europe/Paris consistently.
            Create exactly one booking per candidate.
            Return the final results in JSON format.
            
            Execute the scheduling workflow and return the final booking report.
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
        
            You MUST always use one of the available tools before answering.
        
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

    public static final String CONVERT_CV_PDF_INTO_JSON = """

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
            
            CONSTRAINTS:
            - "languages" must always be an object (use {} if empty).
            - Arrays must always be present (never null).
            - Strings must be valid JSON strings.
            - Keep realistic but concise content.
            
            Rules:
            - no explanation
            - no markdown
            - no numbering
            - JSON only
            Now generate the JSON resume.
            """;
}