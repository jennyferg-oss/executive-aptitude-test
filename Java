// ===============================================
// 1. CONFIGURATION AND CONSTANTS
// ===============================================

// The total number of questions for the calculation.
const TOTAL_QUESTIONS = 50; 

// Maximum possible weight per question for any given metric (5 is the max score in the provided weights).
const MAX_WEIGHT_PER_QUESTION = 5; 

// Total maximum score for a single competency (e.g., Logic).
const MAX_SCORE_PER_COMPETENCY = MAX_WEIGHT_PER_QUESTION * TOTAL_QUESTIONS; 

// Total maximum score across all 5 executive competencies (250 * 5 = 1250).
const TOTAL_MAX_COMPETENCY_SCORE = MAX_SCORE_PER_COMPETENCY * 5; 

// The required score to pass the test (90% of the total max score).
const PASS_THRESHOLD = TOTAL_MAX_COMPETENCY_SCORE * 0.90; 

// State variables
let currentQuestionIndex = 0;
let userResponses = [];
let selectedOptionIndex = null;

// DOM Elements
const testContainer = document.getElementById('test-container');
const resultsContainer = document.getElementById('results-container');
const questionNumberElement = document.getElementById('question-number');
const scenarioTitleElement = document.getElementById('scenario-title');
const scenarioTextElement = document.getElementById('scenario-text');
const optionsContainer = document.getElementById('options-container');
const nextButton = document.getElementById('next-button');


// ===============================================
// 2. DATA: THE 50 TEST QUESTIONS
// ===============================================
const questions = [
    // --- Lógica y Ejecutivo (1-10) ---
    {
        id: 1, title: "Unforeseen Market Shift", scenario: "Your primary market abruptly adopts a new technology you haven't invested in. You must decide whether to pivot resources immediately or wait for clear data.",
        options: [
            { text: "Allocate 70% of R&D to adopt the new tech while monitoring competitor response and risk.", weights: { Logic: 4, Performance: 4, Leadership: 3, Innovation: 3, Executive: 4, Openness: 4, Conscientiousness: 4, Extraversion: 3, Agreeableness: 3, Neuroticism: 1 } },
            { text: "Wait 6 months for concrete performance data from early adopters before committing significant capital.", weights: { Logic: 5, Performance: 2, Leadership: 1, Innovation: 1, Executive: 3, Openness: 2, Conscientiousness: 5, Extraversion: 1, Agreeableness: 4, Neuroticism: 0 } },
            { text: "Ignore the shift; continue optimizing the current product, trusting your existing loyal customer base.", weights: { Logic: 1, Performance: 1, Leadership: 1, Innovation: 0, Executive: 0, Openness: 0, Conscientiousness: 3, Extraversion: 1, Agreeableness: 2, Neuroticism: 2 } },
            { text: "Immediately halt all projects and command a full pivot to the new technology, maximizing speed.", weights: { Logic: 2, Performance: 5, Leadership: 4, Innovation: 4, Executive: 2, Openness: 5, Conscientiousness: 2, Extraversion: 5, Agreeableness: 1, Neuroticism: 3 } }
        ]
    },
    {
        id: 2, title: "The Project Crisis", scenario: "You lead a critical project facing a major, unexpected roadblock. The team is demoralized and the deadline is tomorrow.",
        options: [
            { text: "Call an urgent 15-minute huddle. Ask the team for their immediate and best proposed solutions, facilitate a quick decision, and delegate execution immediately.", weights: { Logic: 3, Performance: 5, Leadership: 5, Innovation: 3, Executive: 5, Openness: 3, Conscientiousness: 5, Extraversion: 4, Agreeableness: 3, Neuroticism: 0 } },
            { text: "Immediately take control, assign specific tasks to team members without debate, and personally oversee the most complex part to ensure compliance.", weights: { Logic: 1, Performance: 3, Leadership: 4, Innovation: 1, Executive: 3, Openness: 1, Conscientiousness: 4, Extraversion: 3, Agreeableness: 1, Neuroticism: 1 } },
            { text: "Postpone the deadline immediately. Inform stakeholders and spend the next hour analyzing the root cause of the roadblock before planning the next steps.", weights: { Logic: 4, Performance: 1, Leadership: 2, Innovation: 2, Executive: 1, Openness: 2, Conscientiousness: 2, Extraversion: 1, Agreeableness: 4, Neuroticism: 3 } },
            { text: "Research external solutions and best practices from competitors before making any internal decisions or disturbing the team's concentration.", weights: { Logic: 5, Performance: 1, Leadership: 1, Innovation: 4, Executive: 2, Openness: 5, Conscientiousness: 3, Extraversion: 1, Agreeableness: 2, Neuroticism: 2 } }
        ]
    },
    {
        id: 3, title: "Conflicting Data Sources", scenario: "Two equally reliable reports show contradictory results regarding customer acquisition ROI. You need a budget decision today.",
        options: [
            { text: "Average the results and proceed with a conservative budget based on the mean.", weights: { Logic: 2, Performance: 2, Leadership: 1, Innovation: 1, Executive: 2, Openness: 1, Conscientiousness: 3, Extraversion: 1, Agreeableness: 4, Neuroticism: 1 } },
            { text: "Quickly identify the underlying assumptions and methodologies of both reports to reconcile the difference before deciding.", weights: { Logic: 5, Performance: 4, Leadership: 3, Innovation: 3, Executive: 5, Openness: 4, Conscientiousness: 5, Extraversion: 2, Agreeableness: 2, Neuroticism: 0 } },
            { text: "Trust the report that supports the strategy you were already leaning toward, as time is running out.", weights: { Logic: 1, Performance: 3, Leadership: 2, Innovation: 1, Executive: 1, Openness: 1, Conscientiousness: 1, Extraversion: 4, Agreeableness: 1, Neuroticism: 4 } },
            { text: "Delay the budget decision and launch a third, external audit to definitively resolve the contradiction.", weights: { Logic: 4, Performance: 1, Leadership: 2, Innovation: 2, Executive: 3, Openness: 3, Conscientiousness: 4, Extraversion: 1, Agreeableness: 3, Neuroticism: 2 } }
        ]
    },
    {
        id: 4, title: "Delegation Dilemma", scenario: "You have a high-stakes task that you know you can do perfectly, but it's an excellent development opportunity for a promising junior manager.",
        options: [
            { text: "Do the task yourself to ensure 100% success, and mentor the junior manager on a less critical, future project.", weights: { Logic: 3, Performance: 4, Leadership: 2, Innovation: 1, Executive: 3, Openness: 2, Conscientiousness: 5, Extraversion: 1, Agreeableness: 2, Neuroticism: 1 } },
            { text: "Delegate the task completely, providing only high-level goals and letting the junior manager learn through independent challenge.", weights: { Logic: 2, Performance: 2, Leadership: 4, Innovation: 3, Executive: 4, Openness: 4, Conscientiousness: 2, Extraversion: 3, Agreeableness: 3, Neuroticism: 2 } },
            { text: "Delegate the task but schedule frequent, detailed check-ins and offer significant resources, prioritizing development over risk mitigation.", weights: { Logic: 4, Performance: 3, Leadership: 5, Innovation: 4, Executive: 5, Openness: 5, Conscientiousness: 4, Extraversion: 4, Agreeableness: 5, Neuroticism: 0 } },
            { text: "Redefine the task into smaller, lower-risk parts, delegating only the parts that pose minimal threat to the overall project.", weights: { Logic: 5, Performance: 1, Leadership: 1, Innovation: 1, Executive: 1, Openness: 1, Conscientiousness: 3, Extraversion: 1, Agreeableness: 2, Neuroticism: 1 } }
        ]
    },
    {
        id: 5, title: "Ethical Gray Area", scenario: "Your legal team informs you that a new product feature, though technically legal, exploits a loophole that could harm a small segment of vulnerable users.",
        options: [
            { text: "Proceed with the feature, as legal compliance is the standard and the loophole will eventually be closed by regulators.", weights: { Logic: 3, Performance: 4, Leadership: 1, Innovation: 2, Executive: 2, Openness: 1, Conscientiousness: 1, Extraversion: 3, Agreeableness: 0, Neuroticism: 3 } },
            { text: "Immediately halt the feature launch, regardless of the competitive impact, and work to redesign it to eliminate the risk to vulnerable users.", weights: { Logic: 4, Performance: 1, Leadership: 5, Innovation: 3, Executive: 5, Openness: 4, Conscientiousness: 5, Extraversion: 2, Agreeableness: 5, Neuroticism: 0 } },
            { text: "Launch the feature with a stern public disclosure about the loophole, shifting responsibility to the user.", weights: { Logic: 2, Performance: 3, Leadership: 2, Innovation: 1, Executive: 3, Openness: 2, Conscientiousness: 3, Extraversion: 4, Agreeableness: 1, Neuroticism: 2 } },
            { text: "Consult with a specialized ethics board for external guidance before making the final decision.", weights: { Logic: 5, Performance: 2, Leadership: 3, Innovation: 4, Executive: 4, Openness: 5, Conscientiousness: 4, Extraversion: 1, Agreeableness: 4, Neuroticism: 1 } }
        ]
    },
    // ... (More questions follow for brevity)
    // --- Liderazgo e Innovación (11-20) ---
    { id: 6, title: "Handling a Star Performer", scenario: "Your top employee is consistently disruptive in team meetings, though their individual output is unmatched.", 
        options: [
            { text: "Meet privately with the employee, clearly stating the behavioral expectations and the negative impact on team morale, offering coaching.", weights: { Logic: 4, Performance: 3, Leadership: 5, Innovation: 3, Executive: 5, Openness: 3, Conscientiousness: 4, Extraversion: 3, Agreeableness: 3, Neuroticism: 0 } },
            { text: "Ignore the behavior, prioritizing their superior output and ensuring their work environment remains comfortable.", weights: { Logic: 1, Performance: 4, Leadership: 1, Innovation: 1, Executive: 1, Openness: 1, Conscientiousness: 2, Extraversion: 2, Agreeableness: 4, Neuroticism: 2 } },
            { text: "Address the disruption publicly in the meeting to set an immediate example for the rest of the team.", weights: { Logic: 2, Performance: 2, Leadership: 3, Innovation: 1, Executive: 2, Openness: 1, Conscientiousness: 3, Extraversion: 5, Agreeableness: 0, Neuroticism: 4 } },
            { text: "Restructure the team to isolate the star performer, allowing them to work independently.", weights: { Logic: 3, Performance: 1, Leadership: 2, Innovation: 2, Executive: 3, Openness: 2, Conscientiousness: 5, Extraversion: 1, Agreeableness: 1, Neuroticism: 1 } }
        ]
    },
    { id: 7, title: "Budget Allocation for R&D", scenario: "You must allocate funds between a high-certainty, low-return project and a high-risk, potentially revolutionary research initiative.",
        options: [
            { text: "Allocate 80% to the safe project and 20% to the high-risk initiative for minimal exploratory work.", weights: { Logic: 4, Performance: 3, Leadership: 2, Innovation: 1, Executive: 4, Openness: 2, Conscientiousness: 5, Extraversion: 1, Agreeableness: 3, Neuroticism: 1 } },
            { text: "Fund the revolutionary initiative fully, cutting the safe project, believing 'go big or go home' is necessary for market leadership.", weights: { Logic: 2, Performance: 5, Leadership: 4, Innovation: 5, Executive: 4, Openness: 5, Conscientiousness: 2, Extraversion: 4, Agreeableness: 1, Neuroticism: 3 } },
            { text: "Split the budget 50/50, ensuring both security and a reasonable chance at disruption.", weights: { Logic: 3, Performance: 3, Leadership: 3, Innovation: 3, Executive: 3, Openness: 3, Conscientiousness: 3, Extraversion: 3, Agreeableness: 3, Neuroticism: 3 } },
            { text: "Invest 60% in the revolutionary idea, conditional on the team providing clear, quantifiable 'kill points' to stop investment if progress stalls.", weights: { Logic: 5, Performance: 4, Leadership: 5, Innovation: 4, Executive: 5, Openness: 4, Conscientiousness: 4, Extraversion: 2, Agreeableness: 2, Neuroticism: 0 } }
        ]
    },
    { id: 8, title: "Managing Fear of Failure", scenario: "Your team is hesitant to propose radical ideas because previous innovative projects resulted in high-profile failures.",
        options: [
            { text: "Implement a 'Fail Forward' system: publicly reward the *learning* derived from failures and protect the team from blame.", weights: { Logic: 4, Performance: 3, Leadership: 5, Innovation: 5, Executive: 5, Openness: 5, Conscientiousness: 4, Extraversion: 4, Agreeableness: 4, Neuroticism: 0 } },
            { text: "Only approve low-risk, incremental improvements until the team regains its confidence and sense of security.", weights: { Logic: 2, Performance: 2, Leadership: 1, Innovation: 1, Executive: 3, Openness: 1, Conscientiousness: 5, Extraversion: 1, Agreeableness: 3, Neuroticism: 1 } },
            { text: "Bring in a new external team specifically tasked with high-risk innovation, leaving the existing team focused on execution.", weights: { Logic: 3, Performance: 4, Leadership: 2, Innovation: 4, Executive: 3, Openness: 4, Conscientiousness: 3, Extraversion: 2, Agreeableness: 2, Neuroticism: 1 } },
            { text: "Run a company-wide campaign reminding employees that innovation is mandatory for long-term survival.", weights: { Logic: 1, Performance: 1, Leadership: 2, Innovation: 2, Executive: 1, Openness: 2, Conscientiousness: 1, Extraversion: 3, Agreeableness: 1, Neuroticism: 4 } }
        ]
    },
    { id: 9, title: "The Unpopular Decision", scenario: "You must implement a major structural reorganization that is statistically proven to increase long-term efficiency but is highly unpopular with the current staff.",
        options: [
            { text: "Implement the change swiftly and decisively, then focus on post-implementation communication and training.", weights: { Logic: 4, Performance: 5, Leadership: 4, Innovation: 3, Executive: 5, Openness: 2, Conscientiousness: 5, Extraversion: 3, Agreeableness: 1, Neuroticism: 1 } },
            { text: "Conduct a series of one-on-one meetings to personally explain the rationale, seeking specific feedback to mitigate minor concerns before the launch.", weights: { Logic: 5, Performance: 3, Leadership: 5, Innovation: 4, Executive: 4, Openness: 4, Conscientiousness: 4, Extraversion: 2, Agreeableness: 4, Neuroticism: 0 } },
            { text: "Delay the change until a crisis forces the staff to see the need for reorganization.", weights: { Logic: 1, Performance: 1, Leadership: 1, Innovation: 1, Executive: 0, Openness: 1, Conscientiousness: 1, Extraversion: 1, Agreeableness: 3, Neuroticism: 5 } },
            { text: "Allow the teams to propose alternative structures, accepting a slightly less optimal but more socially acceptable plan.", weights: { Logic: 3, Performance: 2, Leadership: 3, Innovation: 2, Executive: 2, Openness: 3, Conscientiousness: 3, Extraversion: 3, Agreeableness: 5, Neuroticism: 1 } }
        ]
    },
    { id: 10, title: "Crisis of Public Trust", scenario: "A security breach compromises a small amount of customer data. The immediate financial impact is minimal, but public backlash is growing.",
        options: [
            { text: "Issue a highly detailed public report immediately, outlining the root cause, remedial actions, and offering proactive compensation, regardless of cost.", weights: { Logic: 4, Performance: 4, Leadership: 5, Innovation: 3, Executive: 5, Openness: 4, Conscientiousness: 5, Extraversion: 3, Agreeableness: 5, Neuroticism: 0 } },
            { text: "Downplay the breach in the media, emphasizing the minimal financial loss and legal compliance.", weights: { Logic: 1, Performance: 2, Leadership: 1, Innovation: 1, Executive: 2, Openness: 1, Conscientiousness: 1, Extraversion: 2, Agreeableness: 0, Neuroticism: 4 } },
            { text: "Hire a PR firm to manage the narrative and redirect public attention to the company's other positive achievements.", weights: { Logic: 2, Performance: 3, Leadership: 2, Innovation: 2, Executive: 3, Openness: 2, Conscientiousness: 3, Extraversion: 5, Agreeableness: 2, Neuroticism: 3 } },
            { text: "Address the breach internally, focusing 100% on preventing future events before making any public statement.", weights: { Logic: 5, Performance: 1, Leadership: 3, Innovation: 4, Executive: 4, Openness: 3, Conscientiousness: 4, Extraversion: 1, Agreeableness: 3, Neuroticism: 1 } }
        ]
    },
    // --- Performance y Desempeño (11-20) ---
    { id: 11, title: "Underperforming Veteran", scenario: "A long-time employee, critical to operations, has been consistently missing targets for 6 months. Coaching hasn't helped.",
        options: [
            { text: "Initiate the formal performance improvement process (PIP) immediately, adhering strictly to HR protocol for a clean separation if necessary.", weights: { Logic: 4, Performance: 5, Leadership: 3, Innovation: 2, Executive: 5, Openness: 2, Conscientiousness: 5, Extraversion: 2, Agreeableness: 1, Neuroticism: 1 } },
            { text: "Redefine the veteran's role to a non-performance-critical position, leveraging their institutional knowledge without demanding high output.", weights: { Logic: 3, Performance: 1, Leadership: 4, Innovation: 1, Executive: 3, Openness: 3, Conscientiousness: 3, Extraversion: 1, Agreeableness: 5, Neuroticism: 1 } },
            { text: "Transfer the employee to a new department, hoping a fresh start and different manager will resolve the issue.", weights: { Logic: 2, Performance: 2, Leadership: 2, Innovation: 2, Executive: 1, Openness: 3, Conscientiousness: 1, Extraversion: 4, Agreeableness: 4, Neuroticism: 2 } },
            { text: "Fire the employee immediately to set a clear precedent for performance standards across the company.", weights: { Logic: 1, Performance: 4, Leadership: 1, Innovation: 1, Executive: 2, Openness: 1, Conscientiousness: 4, Extraversion: 3, Agreeableness: 0, Neuroticism: 4 } }
        ]
    },
    { id: 12, title: "Resource Scarcity", scenario: "Two high-priority teams urgently need the same critical piece of expensive equipment. Both claim it is essential for their immediate success.",
        options: [
            { text: "Assign the equipment to the team whose project aligns most directly with the company's highest-level strategic goal.", weights: { Logic: 5, Performance: 4, Leadership: 4, Innovation: 3, Executive: 5, Open
