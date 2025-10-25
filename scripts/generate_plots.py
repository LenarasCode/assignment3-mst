import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

# Настройка стиля графиков
plt.style.use('seaborn-v0_8')
sns.set_palette("husl")

# Загрузка данных
df = pd.read_csv('results/performance_data.csv')

# Создание графиков
fig, axes = plt.subplots(2, 2, figsize=(15, 12))
fig.suptitle('Performance Analysis: Prim vs Kruskal MST Algorithms', fontsize=16, fontweight='bold')

# График 1: Время выполнения по размеру графа
axes[0, 0].plot(df['Vertices'], df['Prim_Time_ms'], 'o-', label='Prim\'s Algorithm', linewidth=2, markersize=6)
axes[0, 0].plot(df['Vertices'], df['Kruskal_Time_ms'], 's-', label='Kruskal\'s Algorithm', linewidth=2, markersize=6)
axes[0, 0].set_xlabel('Number of Vertices')
axes[0, 0].set_ylabel('Execution Time (ms)')
axes[0, 0].set_title('Execution Time vs Graph Size')
axes[0, 0].legend()
axes[0, 0].grid(True, alpha=0.3)

# График 2: Количество операций по размеру графа
axes[0, 1].plot(df['Vertices'], df['Prim_Operations'], 'o-', label='Prim\'s Algorithm', linewidth=2, markersize=6)
axes[0, 1].plot(df['Vertices'], df['Kruskal_Operations'], 's-', label='Kruskal\'s Algorithm', linewidth=2, markersize=6)
axes[0, 1].set_xlabel('Number of Vertices')
axes[0, 1].set_ylabel('Number of Operations')
axes[0, 1].set_title('Operations Count vs Graph Size')
axes[0, 1].legend()
axes[0, 1].grid(True, alpha=0.3)

# График 3: Сравнение времени выполнения по типам графов
graph_types = df['Graph_Type'].unique()
prim_times = [df[df['Graph_Type'] == gt]['Prim_Time_ms'].mean() for gt in graph_types]
kruskal_times = [df[df['Graph_Type'] == gt]['Kruskal_Time_ms'].mean() for gt in graph_types]

x = np.arange(len(graph_types))
width = 0.35

axes[1, 0].bar(x - width/2, prim_times, width, label='Prim\'s Algorithm', alpha=0.8)
axes[1, 0].bar(x + width/2, kruskal_times, width, label='Kruskal\'s Algorithm', alpha=0.8)
axes[1, 0].set_xlabel('Graph Type')
axes[1, 0].set_ylabel('Average Execution Time (ms)')
axes[1, 0].set_title('Average Execution Time by Graph Type')
axes[1, 0].set_xticks(x)
axes[1, 0].set_xticklabels(graph_types)
axes[1, 0].legend()
axes[1, 0].grid(True, alpha=0.3)

# График 4: Соотношение производительности
performance_ratio = df['Prim_Time_ms'] / df['Kruskal_Time_ms']
axes[1, 1].scatter(df['Vertices'], performance_ratio, alpha=0.7, s=50)
axes[1, 1].axhline(y=1, color='red', linestyle='--', alpha=0.7, label='Equal Performance')
axes[1, 1].set_xlabel('Number of Vertices')
axes[1, 1].set_ylabel('Performance Ratio (Prim/Kruskal)')
axes[1, 1].set_title('Performance Ratio: Prim vs Kruskal')
axes[1, 1].legend()
axes[1, 1].grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('results/performance_analysis.png', dpi=300, bbox_inches='tight')
plt.show()

# Создание дополнительного графика: Сравнение сложности
fig2, ax = plt.subplots(1, 1, figsize=(10, 6))

# Теоретическая сложность
vertices = np.linspace(10, 50, 100)
prim_theoretical = vertices * np.log2(vertices)  # O(V log V)
kruskal_theoretical = vertices * np.log2(vertices)  # O(E log E) ≈ O(V log V) для плотных графов

ax.plot(vertices, prim_theoretical, '--', label='Prim\'s Theoretical O(V log V)', linewidth=2)
ax.plot(vertices, kruskal_theoretical, '--', label='Kruskal\'s Theoretical O(E log E)', linewidth=2)

# Экспериментальные данные
ax.scatter(df['Vertices'], df['Prim_Operations'], alpha=0.7, s=50, label='Prim\'s Experimental')
ax.scatter(df['Vertices'], df['Kruskal_Operations'], alpha=0.7, s=50, label='Kruskal\'s Experimental')

ax.set_xlabel('Number of Vertices')
ax.set_ylabel('Operations Count')
ax.set_title('Theoretical vs Experimental Complexity')
ax.legend()
ax.grid(True, alpha=0.3)

plt.tight_layout()
plt.savefig('results/complexity_analysis.png', dpi=300, bbox_inches='tight')
plt.show()

# Статистический анализ
print("=== Performance Analysis Summary ===")
print(f"Total graphs analyzed: {len(df)}")
print(f"Small graphs (< 30 vertices): {len(df[df['Graph_Type'] == 'Small'])}")
print(f"Medium graphs (< 300 vertices): {len(df[df['Graph_Type'] == 'Medium'])}")
print(f"Large graphs (< 1000 vertices): {len(df[df['Graph_Type'] == 'Large'])}")
print(f"Extra graphs (< 2000 vertices): {len(df[df['Graph_Type'] == 'Extra'])}")

print("\n=== Average Performance Metrics ===")
print(f"Average Prim's execution time: {df['Prim_Time_ms'].mean():.2f} ms")
print(f"Average Kruskal's execution time: {df['Kruskal_Time_ms'].mean():.2f} ms")
print(f"Average Prim's operations: {df['Prim_Operations'].mean():.0f}")
print(f"Average Kruskal's operations: {df['Kruskal_Operations'].mean():.0f}")

print("\n=== Performance Comparison ===")
prim_faster_count = len(df[df['Prim_Time_ms'] < df['Kruskal_Time_ms']])
kruskal_faster_count = len(df[df['Kruskal_Time_ms'] < df['Prim_Time_ms']])
print(f"Prim's algorithm faster in: {prim_faster_count} cases")
print(f"Kruskal's algorithm faster in: {kruskal_faster_count} cases")

print("\n=== Cost Verification ===")
cost_matches = len(df[df['Prim_Cost'] == df['Kruskal_Cost']])
print(f"MST costs match in all {cost_matches} cases: {'✓' if cost_matches == len(df) else '✗'}")
