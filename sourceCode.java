import java.util.Random;

public class Escape_from_the_Maze 
{
    static class MyStack<T> implements Iterable<T>
    {
        private Object[] stack;
        private int capacity;
        private int top;

        public MyStack(int size)
        {
            capacity = size;
            stack = new Object[capacity];
            top = -1;
        }
        public boolean isFull()
        {
            return top == capacity - 1;
        }

        public boolean isEmpty()
        {
            return top == -1;
        }

        public void push(T value)
        {
            if(isFull())
            {
                System.out.println("No empty space!");
                return;
            }

            stack[++top] = value;
        }
        @SuppressWarnings("unchecked")
        public T pop()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return null;
            }
            T value = (T) stack[top];
            top--;
            return value;
        }
        @SuppressWarnings("unchecked")
        public T peek()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return null;
            }

            return (T) stack[top];
        }

        public void printStack()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return;
            }

            for(int i = top; i >= 0; i--)
            {
                System.out.println(i);
            }
        }

        public int size()
        {
            return top+1;
        }

        @Override
        public java.util.Iterator<T> iterator() {
            return new java.util.Iterator<T>() {
                private int currentIndex = top;

                @Override
                public boolean hasNext() {
                    return currentIndex >= 0;
                }

                @Override
                @SuppressWarnings("unchecked")
                public T next() {
                    if (!hasNext()) {
                        throw new java.util.NoSuchElementException();
                    }
                    return (T) stack[currentIndex--];
                }
            };
        }
    }

    static class MyQueue<T>
    {
        private static class Node<T>
        {
            private T data;
            private Node<T> next;

            public Node(T data)
            {
                this.data = data;
                this.next = null;
            }
        }

        private Node<T> front;
        private Node<T> rear;

        public MyQueue()
        {
            front = null;
            rear = null;
        }

        public boolean isEmpty()
        {
            return front == null;
        }

        public void enqueue(T value)
        {
            Node<T> newNode = new Node<>(value);
            if(rear == null)
            {
                front = newNode;
                rear = newNode;
                return;
            }

            rear.next = newNode;
            rear = newNode;
        }

        public T dequeue()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return null;
            }

            T data = front.data;
            front = front.next;

            if(front == null)
            {
                rear = null;
            }

            return data;
        }

        public T peek()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return null;
            }

            return front.data;
        }

        public void printQueue()
        {
            if(isEmpty())
            {
                System.out.println("Empty");
                return;
            }

            Node<T> current = front;

            while(current.next != null)
            {
                System.out.println(current.data + " -> ");
                current = current.next;
            }
        }
    } 

    static class MyArrayList<T> implements Iterable<T> {
        private Object[] array;
        private int size;
        private int capacity;

        public MyArrayList() {
            this.capacity = 10;
            this.array = new Object[capacity];
            this.size = 0;
        }

        public void add(T element) {
            if (size == capacity) {
                capacity *= 2;
                Object[] newArray = new Object[capacity];
                for (int i = 0; i < size; i++) {
                    newArray[i] = array[i];
                }
                array = newArray;
            }
            array[size++] = element;
        }

        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            @SuppressWarnings("unchecked")
            T element = (T) array[index];
            return element;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void clear() {
            size = 0;
        }

        @Override
        public java.util.Iterator<T> iterator() {
            return new java.util.Iterator<T>() {
                private int currentIndex = 0;

                @Override
                public boolean hasNext() {
                    return currentIndex < size;
                }

                @Override
                @SuppressWarnings("unchecked")
                public T next() {
                    if (!hasNext()) {
                        throw new java.util.NoSuchElementException();
                    }
                    return (T) array[currentIndex++];
                }
            };
        }
    }

    static class Maze_tile {
        int x;
        int y;
        char tile_type;
        boolean agent;

        public Maze_tile(int x, int y, char tile_type)
        {
            this.x = x;
            this.y = y;
            this.tile_type = tile_type;
            this.agent = false;
        }

        public boolean Is_traversable() 
        {
            return tile_type != 'W' && !agent;
        }

        public String To_string() 
        {
            if (agent)
            {
                return "A";
            }

            switch (tile_type) 
            {
                case 'W': return "W";
                case 'T': return "T";
                case 'P': return "P";
                case 'G': return "G";
                case 'S': return "S";
                case 'E': return "E";
                default: return "*";
            }
        }
    }

    static class Agent 
    {
        int id, x, y;
        MyStack<String> Move_history;
        boolean Has_power_up, Reached_goal;
        int steps, backtracks, traps;

        public Agent(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.Has_power_up = false;
            this.Reached_goal = false;
            this.steps = 0;
            this.backtracks = 0;
            this.traps = 0;
            this.Move_history = new MyStack<>(100);
            Record_move();
        }

        public void Record_move() {
            Move_history.push(x + "," + y);
        }

        public void Move(String dir, Maze_manager maze) {
            int new_x = x, new_y = y;

            switch (dir) 
            {
                case "UP": new_x--;
                case "DOWN": new_x++;
                case "LEFT": new_y--;
                case "RIGHT": new_y++;
            }

            if (maze.Is_valid_move(x, y, dir)) {
                maze.Update_agent(this, x, y);
                x = new_x;
                y = new_y;
                steps++;
                Record_move();
                if (maze.Get_tile(x, y).tile_type == 'G') Reached_goal = true;
            }
        }

        public void Backtrack(Maze_manager maze) {
            if (Move_history.size() < 2) return;
            Move_history.pop();
            String[] prev = Move_history.pop().split(",");
            int prev_x = Integer.parseInt(prev[0]);
            int prev_y = Integer.parseInt(prev[1]);

            maze.Update_agent(this, x, y);
            x = prev_x;
            y = prev_y;
            maze.Get_tile(x, y).agent = true;
            backtracks++;
        }
    }

    static class Maze_manager {
        Maze_tile[][] grid;
        int width, height;
        MyArrayList<Agent> agents;
        MyArrayList<Integer> rotating_rows;
        Random rand;
        int goal_x, goal_y;
        MyArrayList<int[]> start_positions;

        public Maze_manager(int width, int height) {
            this.width = width;
            this.height = height;
            this.grid = new Maze_tile[width][height];
            this.agents = new MyArrayList<>();
            this.rotating_rows = new MyArrayList<>();
            this.start_positions = new MyArrayList<>();
            this.rand = new Random();
            Generate_maze();
        }

        public void Generate_maze() {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    char tile_type = 'E';
                    if (i == 0 || j == 0 || i == width - 1 || j == height - 1)
                        tile_type = 'W';
                    else {
                        double r = rand.nextDouble();
                        if (r < 0.1) tile_type = 'W';
                        else if (r < 0.2) tile_type = 'T';
                        else if (r < 0.25) tile_type = 'P';
                    }
                    grid[i][j] = new Maze_tile(i, j, tile_type);
                }
            }

            goal_x = 1 + rand.nextInt(width - 2);
            goal_y = 1 + rand.nextInt(height - 2);
            grid[goal_x][goal_y].tile_type = 'G';

            int count = 0;
            while (count < 5) {
                int x = 1 + rand.nextInt(width - 2);
                int y = 1 + rand.nextInt(height - 2);
                Maze_tile tile = grid[x][y];
                if (tile.tile_type == 'E') {
                    tile.tile_type = 'S';
                    start_positions.add(new int[]{x, y});
                    count++;
                }
            }

            for (int i = 1; i < width - 1; i += 2)
                rotating_rows.add(i);
        }

        public void rotateCorridor(int row)
        {
            if(grid == null || row < 0 || row >= grid.length)
            {
                return;
            }

            Maze_tile[] current = grid[row];
            int len = current.length;

            if(len == 0)
            {
                return;
            }

            Maze_tile last = current[len - 1];
        
            for (int j = width - 1; j > 0; j--) 
            {
                current[j] = current[j - 1];
            }
            
            current[0] = last;
        }

        public boolean Is_valid_move(int x, int y, String dir) {
            switch (dir) {
                case "UP" : x--;
                case "DOWN" : x++;
                case "LEFT" : y--;
                case "RIGHT" : y++;
            }
            return x >= 0 && x < width && y >= 0 && y < height && grid[x][y].Is_traversable();
        }

        public Maze_tile Get_tile(int x, int y) {
            return grid[x][y];
        }

        public void Update_agent(Agent agent, int old_x, int old_y) {
            grid[old_x][old_y].agent = false;
            grid[agent.x][agent.y].agent = true;
        }

        public void Add_agent(Agent agent) {
            agents.add(agent);
            grid[agent.x][agent.y].agent = true;
        }

        public void Print_maze() {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    System.out.print(grid[i][j].To_string() + " ");
                }
                System.out.println();
            }
        }
    }

    static class Turn_manager {
        MyQueue<Agent> turn_queue;
        MyArrayList<Agent> finished_agents;
        int current_turn, max_turns;

        public Turn_manager(int max_turns) {
            this.turn_queue = new MyQueue<>();
            this.finished_agents = new MyArrayList<>();
            this.max_turns = max_turns;
            this.current_turn = 0;
        }

        public void Add_agent(Agent agent) {
            turn_queue.enqueue(agent);
        }

        public void Next_turn(Maze_manager maze) {
            current_turn++;

            if (current_turn % 3 == 0 && !maze.rotating_rows.isEmpty()) {
                int row = maze.rotating_rows.get(maze.rand.nextInt(maze.rotating_rows.size()));
                maze.rotateCorridor(row);
                System.out.println("Row " + row + " rotated");
            }

            Agent agent = turn_queue.dequeue();
            if (agent == null) return;

            if (agent.Reached_goal) {
                finished_agents.add(agent);
                System.out.println("Agent " + agent.id + " already reached the goal!");
                return;
            }

            Process_agent(agent, maze);
            if (!agent.Reached_goal)
                turn_queue.enqueue(agent);
            else {
                finished_agents.add(agent);
                System.out.println("Agent " + agent.id + " reached the goal at turn " + current_turn);
            }
        }

        private void Process_agent(Agent agent, Maze_manager maze) {
            String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
            String preferred = Get_preferred_direction(agent, maze.goal_x, maze.goal_y);

            boolean moved = false;
            if (maze.Is_valid_move(agent.x, agent.y, preferred)) {
                agent.Move(preferred, maze);
                moved = true;
            } else {
                for (String dir : directions) {
                    if (maze.Is_valid_move(agent.x, agent.y, dir)) {
                        agent.Move(dir, maze);
                        moved = true;
                        break;
                    }
                }
            }

            if (moved) {
                Maze_tile tile = maze.Get_tile(agent.x, agent.y);
                if (tile.tile_type == 'T') {
                    if (!agent.Has_power_up) {
                        agent.traps++;
                        agent.Backtrack(maze);
                        agent.Backtrack(maze);
                        System.out.println("Agent " + agent.id + " hit a trap");
                    } else {
                        System.out.println("Agent " + agent.id + " avoided a trap with power up");
                    }
                } else if (tile.tile_type == 'P') {
                    agent.Has_power_up = true;
                    tile.tile_type = 'E';
                    System.out.println("Agent " + agent.id + " collected a power up");
                }
                System.out.println("Agent " + agent.id + " moved to (" + agent.x + "," + agent.y + ")");
            }
        }

        private String Get_preferred_direction(Agent agent, int goal_x, int goal_y) {
            if (Math.abs(agent.x - goal_x) > Math.abs(agent.y - goal_y))
                return (agent.x < goal_x) ? "DOWN" : "UP";
            else
                return (agent.y < goal_y) ? "RIGHT" : "LEFT";
        }

        public boolean Is_game_over() {
            return turn_queue.isEmpty() || current_turn >= max_turns;
        }
    }

    public static void main(String[] args) {
        int maze_size = 7;
        int agent_count = 3;
        int max_turns = 100;

        Maze_manager maze = new Maze_manager(maze_size, maze_size);
        Turn_manager manager = new Turn_manager(max_turns);

        if (maze.start_positions.size() < agent_count) {
            System.out.println("Not enough start positions for all agents!");
            return;
        }

        for (int i = 0; i < agent_count; i++) {
            int[] pos = maze.start_positions.get(i);
            int x = pos[0], y = pos[1];

            Agent agent = new Agent(i + 1, x, y);
            maze.Add_agent(agent);
            manager.Add_agent(agent);
        }

        System.out.println("Game started");
        System.out.println("Goal is at (" + maze.goal_x + "," + maze.goal_y + ")");
        maze.Print_maze();

        while (!manager.Is_game_over()) {
            System.out.println("\nTurn " + (manager.current_turn + 1));
            manager.Next_turn(maze);
            maze.Print_maze();
        }

        System.out.println("\nGame Over");
        System.out.println("Total turns: " + manager.current_turn);

        if (manager.finished_agents.isEmpty()) {
            System.out.println("No agents reached the goal");
        } else {
            for (Agent a : manager.finished_agents) {
                System.out.println("Agent " + a.id + " reached the goal in " + a.steps +
                        " steps backtracked " + a.backtracks + " times traps hit: " + a.traps);
                System.out.print("Path: ");
                for (String s : a.Move_history) {
                    System.out.print(" "+s+" ");
                }
                System.out.println();
            }
        }
    }
}
